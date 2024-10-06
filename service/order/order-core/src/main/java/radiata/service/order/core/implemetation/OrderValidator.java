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

    // 주문 상태 "결제 요청 중" 인지 체크 - 주문 직 후 사용 될.
    public void checkStatusIsPaymentRequest(OrderStatus status) {
        if (!status.equals(OrderStatus.PAYMENT_REQUESTED)) {
            throw new BusinessException(ExceptionMessage.INVALID_ORDER_STATUS);
        }
    }

    // "결제 대기 중" 인지 체크 - 결제 완료 후
    public void checkStatusIsPaymentPending(OrderStatus status) {
        if (!status.equals(OrderStatus.PAYMENT_PENDING)) {
            throw new BusinessException(ExceptionMessage.INVALID_ORDER_STATUS);
        }
    }
}
