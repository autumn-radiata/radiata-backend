package radiata.service.order.core.service;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import radiata.common.domain.brand.request.ProductDeductRequestDto;
import radiata.common.domain.coupon.constant.CouponStatus;
import radiata.common.domain.coupon.dto.request.CouponIssueRollbackRequestDto;
import radiata.common.domain.order.dto.response.StockInfoDto;
import radiata.common.domain.payment.dto.request.PaymentCancelRequestDto;
import radiata.common.domain.timesale.dto.request.TimeSaleProductSaleRollbackRequestDto;
import radiata.common.domain.user.dto.request.PointModifyRequestDto;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.model.entity.OrderItem;
import radiata.service.order.core.service.context.OrderRollbackContext;

@Service
@Slf4j(topic = "Rollback-Service")
@RequiredArgsConstructor
public class RollbackService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // 보상 트랜잭션 - 타임 세일 상품 재고
    private void rollbackTimeSaleStock(List<StockInfoDto> deductedTimeSaleStocks) {
        try {
            // 타임세일 상품 재고 증감 요청
            deductedTimeSaleStocks.forEach(timeSaleProduct -> {
                TimeSaleProductSaleRollbackRequestDto requestDto = new TimeSaleProductSaleRollbackRequestDto(
                    timeSaleProduct.id(), timeSaleProduct.quantity());
                kafkaTemplate.send("timesale.add-stock", requestDto);
            });
        } catch (RuntimeException e) {
            log.error(" [Rollback Error]: TimeSale-Service ");
        }
    }

    // 보상 트랜잭션 - 상품 재고
    private void rollbackProductStock(List<StockInfoDto> deductedProductStocks) {
        try {
            // 상품 재고 증감 요청
            deductedProductStocks.forEach(product -> {
                ProductDeductRequestDto requestDto = new ProductDeductRequestDto(product.id(), product.quantity());
                kafkaTemplate.send("product.add-stock", requestDto);
            });
        } catch (RuntimeException e) {
            log.error(" [Rollback Error]: Product-Service ");
        }
    }

    // 보상 트랜잭션 - 쿠폰 상태
    private void rollbackCoupons(List<String> usedCoupons) {
        try {
            // 쿠폰 상태 변경 요청(USED -> ISSUED)
            usedCoupons.forEach(usedCouponId -> {
                CouponIssueRollbackRequestDto requestDto = new CouponIssueRollbackRequestDto(usedCouponId,
                    CouponStatus.ISSUED);
                kafkaTemplate.send("coupon.rollback-status", requestDto);
            });
        } catch (RuntimeException e) {
            log.error(" [Rollback Error]: Coupon-Service ");
        }
    }

    // 보상 트랜잭션 - 적립금
    private void rollbackRewardPoint(String userId, Integer point) {
        try {
            // 적립금 증감 요청
            PointModifyRequestDto requestDto = new PointModifyRequestDto(userId, point);
            kafkaTemplate.send("user.add-point", requestDto);
        } catch (RuntimeException e) {
            log.error(" [Rollback Error]: User(Point)-Service ");
        }
    }

    // 보상 트랜잭션 - 결제 취소
    private void rollbackPaymentAmount(String paymentId) {
        try {
            // 결제 취소 요청 - (== 환불)
            PaymentCancelRequestDto requestDto = new PaymentCancelRequestDto(paymentId);
            kafkaTemplate.send("payment.cancel", requestDto);
        } catch (RuntimeException e) {
            log.error(" [Rollback Error]: Payment-Service ");
        }
    }


    // 주문 등록 - 보상 트랜잭션
    public void createOrderItemsRollback(OrderRollbackContext context) {
        log.info("### 주문 등록 과정 - 보상 트랜잭션 수행 시작");
        rollbackTimeSaleStock(context.getDeductedTimeSales());
        rollbackProductStock(context.getDeductedProducts());
        rollbackCoupons(context.getUsedCoupons());
    }

    // 주문 취소 - 보상 트랜잭션
    public void cancelOrderItemsRollback(Order order) {

        OrderRollbackContext rollbackContext = collectOrderItemInfo(order.getOrderItems());

        log.info("### 주문 취소 or 미결제 주문 취소 시작 ### ");
        // TODO - 타임세일 시간 끝나면 복구 X
        rollbackTimeSaleStock(rollbackContext.getDeductedTimeSales());
        rollbackProductStock(rollbackContext.getDeductedProducts());
        rollbackCoupons(rollbackContext.getUsedCoupons());

        checkUsedPointAndReward(order.getUserId(), order.getUsedPoint());
        checkPaymentStatusAndRollback(order.getStatus(), order.getPaymentId());
    }

    // 환불
    public void refundRollback(Order order) {

        OrderRollbackContext rollbackContext = collectOrderItemInfo(order.getOrderItems());

        rollbackPaymentAmount(order.getPaymentId());
        // TODO - 타임세일 시간 끝나면 복구 X
        rollbackTimeSaleStock(rollbackContext.getDeductedTimeSales());
        rollbackProductStock(rollbackContext.getDeductedProducts());
    }

    private void checkPaymentStatusAndRollback(OrderStatus status, String paymentId) {

        if (status.equals(OrderStatus.PAYMENT_COMPLETED) || status.equals(OrderStatus.PAYMENT_CANCEL_REQUESTED)) {
            rollbackPaymentAmount(paymentId);
        }
    }

    private void checkUsedPointAndReward(String userId, Integer usedPoint) {
        if (usedPoint > 0) {
            rollbackRewardPoint(userId, usedPoint);
        }
    }

    private OrderRollbackContext collectOrderItemInfo(Set<OrderItem> orderItems) {
        OrderRollbackContext rollbackContext = new OrderRollbackContext();

        for (OrderItem orderItem : orderItems) {
            int quantity = orderItem.getQuantity();
            rollbackContext.addDeductedTimeSale(orderItem.getTimeSaleProductId(), quantity);
            rollbackContext.addDeductedProduct(orderItem.getProductId(), quantity);
            rollbackContext.addUsedCoupon(orderItem.getCouponIssuedId());
        }
        return rollbackContext;
    }
}