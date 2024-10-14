package radiata.service.order.core.implemetation;

import java.util.Objects;
import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;

@Implementation
public class OrderValidator {

    // 유저와 주문 유저 일치 여부 검사
    public void validateUserOwnsOrder(String orderUserId, String userId) {
        if (!orderUserId.equals(userId)) {
            throw new BusinessException(ExceptionMessage.NOT_AUTHORIZED);
        }
    }

    // 결제 요청 금액 == 주문 금액 체크
    public void IsEqualsOrderPrice(Integer amount, Integer orderPrice) {
        if (!Objects.equals(amount, orderPrice)) {
            throw new BusinessException(ExceptionMessage.NOT_EQUALS_PRICE);
        }
    }

    // 결제 취소 가능 여부 판단 -> "결제 단계" 만 취소 가능
    public void checkStatusIsPaymentLevel(OrderStatus status) {
        if (!status.equals(OrderStatus.PAYMENT_COMPLETED)
            && !status.equals(OrderStatus.PAYMENT_PENDING)
            && !status.equals(OrderStatus.PAYMENT_REQUESTED)) {
            throw new BusinessException(ExceptionMessage.IMPOSSIBLE_CANCEL_ORDER_PAYMENT);
        }
    }

    // "결제 취소 요청" 만 -> "결제 취소 완료" 가능
    public void checkStatusIsPaymentCancelRequested(OrderStatus status) {
        if (!status.equals(OrderStatus.PAYMENT_CANCEL_REQUESTED)) {
            throw new BusinessException(ExceptionMessage.IMPOSSIBLE_CANCEL_ORDER_PAYMENT);
        }
    }

    //
    public void checkCanMoveToNextStatus(Order order, OrderStatus nextStatus) {
        OrderStatus currentStatus = order.getStatus();
        // 주문 상태 변경이 가능한 상태인지 확인
        if (!currentStatus.canMoveToNextStatus(nextStatus)) {
            throw new BusinessException(ExceptionMessage.INVALID_ORDER_STATUS);
        }
        order.updateOrderStatus(nextStatus);
    }
}
