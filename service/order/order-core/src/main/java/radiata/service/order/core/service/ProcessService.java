package radiata.service.order.core.service;

import feign.FeignException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import radiata.common.domain.brand.request.ProductDeductRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;
import radiata.common.domain.order.dto.request.OrderEasyPaymentRequestDto;
import radiata.common.domain.order.dto.request.OrderTossPaymentRequestDto;
import radiata.common.domain.order.dto.response.CouponInfoDto;
import radiata.common.domain.payment.constant.PaymentType;
import radiata.common.domain.payment.dto.request.EasyPayCreateRequestDto;
import radiata.common.domain.payment.dto.request.TossPaymentCreateRequestDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.service.client.CouponIssueClient;
import radiata.service.order.core.service.client.PaymentClient;
import radiata.service.order.core.service.client.ProductClient;
import radiata.service.order.core.service.client.TimeSaleProductClient;

@Service
@Slf4j(topic = "Process-Service")
@RequiredArgsConstructor
public class ProcessService {

    private final TimeSaleProductClient timeSaleProductClient;
    private final ProductClient productClient;
    private final CouponIssueClient couponIssueClient;
    private final PaymentClient paymentClient;
    private final RollbackService rollbackService;


    // 타임세일상품 재고 확인 및 차감 요청
    public void checkAndDeductTimeSaleProduct(List<String> deductedTimeSales, String timeSaleProductId) {
        if (timeSaleProductId != null) {
            timeSaleProductClient.checkAndDeductTimeSaleProduct(timeSaleProductId);
            deductedTimeSales.add(timeSaleProductId);
        }
    }

    // 상품 재고 확인 및 차감 요청
    public void checkAndDeductStock(List<String> deductedProducts, String productId, int quantity) {
        productClient.checkAndDeductStock(new ProductDeductRequestDto(productId, quantity));
        deductedProducts.add(productId);
    }

    // 쿠폰 확인 및 상태변경 요청
    public CouponInfoDto checkAndUseCoupon(List<String> usedCoupons, String couponIssuedId, String userId) {
        String saleType = null;
        Integer discountValue = null;
        if (couponIssuedId != null) {
            String couponId = couponIssueClient.useCouponIssue(couponIssuedId, userId).data().couponId();
            usedCoupons.add(couponIssuedId);

            CouponResponseDto couponInfo = couponIssueClient.getCouponType(couponId).data();
            saleType = couponInfo.couponSaleType();
            discountValue = checkCouponSaleType(couponInfo);
        }
        return CouponInfoDto.of(saleType, discountValue);
    }

    // 결제 승인 요청 - 간편결제
    public void requestEasyPayment(OrderEasyPaymentRequestDto requestDto, Order order, String userId) {
        try {
            EasyPayCreateRequestDto paymentRequestDto = createEasyPayRequestDto(userId, requestDto);
            String paymentId = paymentClient.requestEasyPay(paymentRequestDto).data().paymentId();
            order.setPaymentIdAndType(paymentId, PaymentType.TOSS_PAYMENTS);
        } catch (FeignException e) {
            // TODO -> 주문 취소 처리 - 보상 트랜잭션(롤백)
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
            // TODO -> 주문 취소 처리 - 보상 트랜잭션(롤백)
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
