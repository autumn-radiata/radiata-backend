package radiata.service.order.core.implemetation;

import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.order.core.domain.model.constant.OrderStatus;

@Implementation
public class OrderValidator {

    // 유저와 주문 유저 일치 여부 검사
    public void validateUserOwnsOrder(String orderUserId, String userId) {
        if (!orderUserId.equals(userId)) {
            throw new BusinessException(ExceptionMessage.NOT_AUTHORIZED);
        }
    }

    // "결제 요청 중" 만 -> "결제 대기 중" 로 수정 가능
    public void checkStatusIsPaymentRequested(OrderStatus status) {
        if (!status.equals(OrderStatus.PAYMENT_REQUESTED)) {
            throw new BusinessException(ExceptionMessage.INVALID_ORDER_STATUS);
        }
    }

    // "결제 대기 중" 만 -> "결제 완료" 로 수정 가능
    public void checkStatusIsPaymentPending(OrderStatus status) {
        if (!status.equals(OrderStatus.PAYMENT_PENDING)) {
            throw new BusinessException(ExceptionMessage.INVALID_ORDER_STATUS);
        }
    }

    // "결제 완료" 만 -> "배송 대기 중" 으로 가능
    public void checkStatusIsPaymentCompleted(OrderStatus status) {
        if (!status.equals(OrderStatus.PAYMENT_COMPLETED)) {
            throw new BusinessException(ExceptionMessage.INVALID_ORDER_STATUS);
        }
    }
}
