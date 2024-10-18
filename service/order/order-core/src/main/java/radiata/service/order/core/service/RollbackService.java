package radiata.service.order.core.service;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import radiata.service.order.core.domain.model.entity.OrderItem;

@Service
@Slf4j(topic = "Rollback-Service")
@RequiredArgsConstructor
public class RollbackService {

    // 보상 트랜잭션 - 타임 세일 상품 재고
    private void rollbackTimeSaleStock(List<String> deductedTimeSaleStocks) {
        try {
            // 타임세일 상품 재고 증감 요청
            deductedTimeSaleStocks.forEach(timeSaleProduct -> {
                // TODO - kafka 를 이용한 비동기 처리
            });
        } catch (RuntimeException e) {
            log.info(" [Rollback Error]: TimeSale-Service ");
        }
    }

    // 보상 트랜잭션 - 상품 재고
    private void rollbackProductStock(List<String> deductedProductStocks) {
        try {
            // 상품 재고 증감 요청
            deductedProductStocks.forEach(product -> {
                // TODO - kafka 를 이용한 비동기 처리
            });
        } catch (RuntimeException e) {
            log.info(" [Rollback Error]: Product-Service ");
        }
    }

    // 보상 트랜잭션 - 쿠폰 상태
    private void rollbackCoupons(List<String> usedCoupons) {
        try {
            // 쿠폰 상태 변경 요청(USED -> ISSUED)
            usedCoupons.forEach(usedCouponId -> {
                // TODO - kafka 를 이용한 비동기 처리
            });
        } catch (RuntimeException e) {
            log.info(" [Rollback Error]: Coupon-Service ");
        }
    }

    // 보상 트랜잭션 - 적립금
    private void rollbackRewardPoint(String userId) {
        try {
            // 적립금 증감 요청
            // TODO - kafka 를 이용한 비동기 처리
        } catch (RuntimeException e) {
            log.info(" [Rollback Error]: User(Point)-Service ");
        }
    }

    private void rollbackPaymentAmount(String paymentId) {
        try {
            // 결제 취소 요청 - (== 환불)

        } catch (RuntimeException e) {
            log.info(" [Rollback Error]: Payment-Service ");
        }
    }


    // 주문 등록 - 보상 트랜잭션
    public void createOrderItemsRollbackTransaction(List<String> deductedTimeSales, List<String> deductedProducts,
        List<String> usedCoupons) {

        rollbackTimeSaleStock(deductedTimeSales);
        rollbackProductStock(deductedProducts);
        rollbackCoupons(usedCoupons);
    }

    // 주문 취소 - 보상 트랜잭션
    public void cancelOrderItemsRollback(Set<OrderItem> orderItems) {
        
        // 타임세일 재고 롤백
        // 상품 재고 롤백
        // 쿠폰 상태 롤백
        // 적립금?
    }

    public void refundPaymentAmountRollback(Set<OrderItem> orderItems, String paymentId) {
        rollbackPaymentAmount(paymentId);
        // 타임세일 재고 롤백
        // 상품 재고 롤백
        // 쿠폰 상태 롤백
        // 적립금?
    }
}
