package radiata.service.order.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import radiata.common.domain.brand.request.ProductDeductRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;
import radiata.common.domain.order.dto.request.OrderEasyPaymentRequestDto;
import radiata.common.domain.order.dto.request.OrderTossPaymentRequestDto;
import radiata.common.domain.payment.constant.PaymentType;
import radiata.common.domain.payment.dto.request.EasyPayCreateRequestDto;
import radiata.common.domain.payment.dto.request.TossPaymentCreateRequestDto;
import radiata.common.domain.timesale.dto.request.TimeSaleProductSaleRequestDto;
import radiata.common.domain.user.dto.request.PointModifyRequestDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.model.entity.OrderItem;
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
    private final ObjectMapper objectMapper;


    // 타임세일상품 재고 확인 및 차감 요청
    public void checkAndDeductTimeSaleProduct(OrderRollbackContext context, OrderItem orderItem) {
        try {
            String timeSaleProductId = orderItem.getTimeSaleProductId();
            int quantity = orderItem.getQuantity();

            if (timeSaleProductId != null) {
                int discountRate = timeSaleProductClient.checkAndDeductTimeSaleProduct(timeSaleProductId,
                    new TimeSaleProductSaleRequestDto(quantity)).data().discountRate();
                context.addDeductedTimeSale(timeSaleProductId, quantity);
                orderItem.setPrice("RATE", discountRate);
            }
        } catch (FeignException e) {
            log.error("[TimeSale-Service FeignException]: Deduct TimeSale Product Request Error");
            rollbackService.createOrderItemsRollback(context);
            Map<String, String> errorInfo = parseFeignExceptionMessage(e);
            throw new BusinessException(HttpStatus.valueOf(e.status()), errorInfo);
        }
    }

    // 상품 재고 확인 및 차감 요청
    public void checkAndDeductStock(OrderRollbackContext context, OrderItem orderItem) {
        try {
            String productId = orderItem.getProductId();
            int quantity = orderItem.getQuantity();

            productClient.checkAndDeductStock(new ProductDeductRequestDto(productId, quantity));
            context.addDeductedProduct(productId, quantity);
        } catch (FeignException e) {
            log.error("[Brand(Product)-Service FeignException]: DeductStock Request Error");
            rollbackService.createOrderItemsRollback(context);
            Map<String, String> errorInfo = parseFeignExceptionMessage(e);
            throw new BusinessException(HttpStatus.valueOf(e.status()), errorInfo);
        }
    }

    // 쿠폰 확인 및 상태변경 요청
    public void checkAndUseCoupon(OrderRollbackContext context, String userId, OrderItem orderItem) {
        try {
            String couponIssuedId = orderItem.getCouponIssuedId();

            if (couponIssuedId != null) {
                String couponId = couponIssueClient.useCouponIssue(couponIssuedId, userId).data().couponId();
                context.addUsedCoupon(couponIssuedId);

                CouponResponseDto couponInfo = couponIssueClient.getCouponType(couponId).data();
                String saleType = couponInfo.couponSaleType();
                int discountValue = checkCouponSaleType(couponInfo);
                orderItem.setPrice(saleType, discountValue);
            }
        } catch (FeignException e) {
            log.error("[Coupon-Service FeignException]: UseCoupon Request Error");
            rollbackService.createOrderItemsRollback(context);
            Map<String, String> errorInfo = parseFeignExceptionMessage(e);
            throw new BusinessException(HttpStatus.valueOf(e.status()), errorInfo);
        }
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
            rollbackService.createOrderItemsRollback(context);
            Map<String, String> errorInfo = parseFeignExceptionMessage(e);
            throw new BusinessException(HttpStatus.valueOf(e.status()), errorInfo);
        }
    }

    // 결제 승인 요청 - 간편결제
    public void requestEasyPayment(OrderEasyPaymentRequestDto requestDto, Order order, String userId) {
        try {
            EasyPayCreateRequestDto paymentRequestDto = createEasyPayRequestDto(userId, requestDto);
            String paymentId = paymentClient.requestEasyPay(paymentRequestDto).data().paymentId();
            order.setPaymentIdAndType(paymentId, PaymentType.EASY_PAY);
        } catch (FeignException e) {
            log.error("[Payment-Service FeignException]: EasyPay Request Error");
            rollbackService.cancelOrderItemsRollback(order);
            Map<String, String> errorInfo = parseFeignExceptionMessage(e);
            throw new BusinessException(HttpStatus.valueOf(e.status()), errorInfo);
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
            rollbackService.cancelOrderItemsRollback(order);
            Map<String, String> errorInfo = parseFeignExceptionMessage(e);
            throw new BusinessException(HttpStatus.valueOf(e.status()), errorInfo);
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
            requestDto.paymentKey(),
            requestDto.amount().longValue());
    }

    // 주문 취소 요청
    public void processCancelOrder(Order order) {
        // 상태 -> 주문 취소로 변경
        order.updateOrderStatus(OrderStatus.PAYMENT_CANCEL_REQUESTED);
        // 주문 관련 롤백 요청
        rollbackService.cancelOrderItemsRollback(order);
    }

    // FeignException -> status, message, code 추출
    private Map<String, String> parseFeignExceptionMessage(FeignException e) {
        String responseJson = e.contentUTF8();
        Map<String, String> responseMap = new HashMap<>();

        try {
            // JSON을 파싱해서 message와 code 추출
            Map<String, Object> parsedJson = objectMapper.readValue(responseJson,
                new TypeReference<Map<String, Object>>() {
                });

            String message = (String) parsedJson.get("message");
            String code = (String) parsedJson.get("code");

            responseMap.put("message", message);
            responseMap.put("code", code);

        } catch (JsonProcessingException jsonException) {
            log.error("Error parsing Feign response: {}", jsonException.getMessage());
            throw new BusinessException(ExceptionMessage.FEIGN_CLIENT_PARSE_ERROR);
        }

        return responseMap;
    }
}
