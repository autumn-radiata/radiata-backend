package radiata.service.order.core.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import radiata.common.domain.brand.request.ProductDeductRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;
import radiata.common.domain.order.dto.request.OrderEasyPaymentRequestDto;
import radiata.common.domain.order.dto.request.OrderTossPaymentRequestDto;
import radiata.common.domain.order.dto.response.CouponInfoDto;
import radiata.common.domain.payment.constant.PaymentType;
import radiata.common.domain.payment.dto.request.EasyPayCreateRequestDto;
import radiata.common.domain.payment.dto.request.TossPaymentCreateRequestDto;
import radiata.common.domain.user.dto.request.PointModifyRequestDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.service.client.CouponIssueClient;
import radiata.service.order.core.service.client.PaymentClient;
import radiata.service.order.core.service.client.ProductClient;
import radiata.service.order.core.service.client.TimeSaleProductClient;
import radiata.service.order.core.service.client.UserClient;
import radiata.service.order.core.service.context.OrderRollbackContext;

@Service
@Slf4j(topic = "Process-Service")
@RequiredArgsConstructor
public class ProcessService {

    private final TimeSaleProductClient timeSaleProductClient;
    private final ProductClient productClient;
    private final CouponIssueClient couponIssueClient;
    private final PaymentClient paymentClient;
    private final UserClient userClient;
    private final RollbackService rollbackService;


    // 타임세일상품 재고 확인 및 차감 요청
    public void checkAndDeductTimeSaleProduct(OrderRollbackContext context, String timeSaleProductId, int quantity) {
        try {
            if (timeSaleProductId != null) {
                timeSaleProductClient.checkAndDeductTimeSaleProduct(timeSaleProductId);
                context.addDeductedTimeSale(timeSaleProductId, quantity);
            }
        } catch (FeignException e) {
            log.error("[TimeSale-Service FeignException]: Deduct TimeSale Product Request Error");
            rollbackService.createOrderItemsRollbackTransaction(context);
            throw new BusinessException(HttpStatus.CONFLICT, e.getMessage(), "5006");
        }
    }

    // 상품 재고 확인 및 차감 요청
    public void checkAndDeductStock(OrderRollbackContext context, String productId, int quantity) {
        try {
            productClient.checkAndDeductStock(new ProductDeductRequestDto(productId, quantity));
            context.addDeductedProduct(productId, quantity);
        } catch (FeignException e) {
            log.error("[Brand(Product)-Service FeignException]: DeductStock Request Error");
            rollbackService.createOrderItemsRollbackTransaction(context);
            throw new BusinessException(HttpStatus.CONFLICT, e.getMessage(), "5007");
        }
    }

    // 쿠폰 확인 및 상태변경 요청
    public CouponInfoDto checkAndUseCoupon(OrderRollbackContext context, String couponIssuedId, String userId) {
        String saleType = null;
        Integer discountValue = null;

        try {
            if (couponIssuedId != null) {
                String couponId = couponIssueClient.useCouponIssue(couponIssuedId, userId).data().couponId();
                context.addUsedCoupon(couponIssuedId);

                CouponResponseDto couponInfo = couponIssueClient.getCouponType(couponId).data();
                saleType = couponInfo.couponSaleType();
                discountValue = checkCouponSaleType(couponInfo);
            }
        } catch (FeignException e) {
            log.error("[Coupon-Service FeignException]: UseCoupon Request Error");
            rollbackService.createOrderItemsRollbackTransaction(context);
            throw new BusinessException(HttpStatus.CONFLICT, e.getMessage(), "5008");
        }
        return CouponInfoDto.of(saleType, discountValue);
    }

    // 적립금 확인 및 차감 요청
    public void checkAndUsePoint(OrderRollbackContext context, Order order, int point, String userId) {
        try {
            if (point > 0) {
                PointModifyRequestDto pointRequestDto = new PointModifyRequestDto(userId, point);
                userClient.checkAndUseRewardPoint(pointRequestDto);
                order.usePoint(point);
            }
        } catch (FeignException e) {
            log.error("[User(Point)-Service FeignException]: UsePoint Request Error");
            rollbackService.createOrderItemsRollbackTransaction(context);
            throw new BusinessException(HttpStatus.CONFLICT, e.getMessage(), "5008");
        }
    }

    // 결제 승인 요청 - 간편결제
    public void requestEasyPayment(OrderEasyPaymentRequestDto requestDto, Order order, String userId) {
        try {
            EasyPayCreateRequestDto paymentRequestDto = createEasyPayRequestDto(userId, requestDto);
            String paymentId = paymentClient.requestEasyPay(paymentRequestDto).data().paymentId();
            order.setPaymentIdAndType(paymentId, PaymentType.TOSS_PAYMENTS);
        } catch (FeignException e) {
            log.error("[Payment-Service FeignException]: EasyPay Request Error");
            rollbackService.cancelOrderItemsRollback(order.getOrderItems());
            throw new BusinessException(ExceptionMessage.ORDER_PAYMENT_FAILED);
        }
    }

    // 결제 승인 요청 - 토스페이
    public void requestTossPayment(OrderTossPaymentRequestDto requestDto, Order order, String userId) {
        try {
            TossPaymentCreateRequestDto paymentRequestDto = createTossRequestDto(order.getId(), userId, requestDto);
            String paymentId = paymentClient.requestTossPayment(paymentRequestDto).data().paymentId();
            order.setPaymentIdAndType(paymentId, PaymentType.TOSS_PAYMENTS);
        } catch (FeignException e) {
            log.error("[Payment-Service FeignException]: TossPay Request Error");
            rollbackService.cancelOrderItemsRollback(order.getOrderItems());
            throw new BusinessException(ExceptionMessage.ORDER_PAYMENT_FAILED);
        }
    }

    private int checkCouponSaleType(CouponResponseDto couponInfo) {
        // TODO - CouponType enum 으로 수정
        if (couponInfo.couponSaleType().equals("AMOUNT")) {
            return couponInfo.discountAmount();
        }
        return couponInfo.discountRate();
    }


    // TODO - 결제요청 createDto 전략 패턴 적용하기
    private EasyPayCreateRequestDto createEasyPayRequestDto(String userId,
        OrderEasyPaymentRequestDto requestDto) {

        return new EasyPayCreateRequestDto(
            userId,
            requestDto.amount().longValue(),
            requestDto.password());
    }

    private TossPaymentCreateRequestDto createTossRequestDto(String orderId, String userId,
        OrderTossPaymentRequestDto requestDto) {

        return new TossPaymentCreateRequestDto(
            orderId,
            userId,
            requestDto.paymentKeyId(),
            requestDto.amount().longValue());
    }
}
