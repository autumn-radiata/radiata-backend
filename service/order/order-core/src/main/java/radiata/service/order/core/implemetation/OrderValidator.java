package radiata.service.order.core.implemetation;

import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;

@Implementation
public class OrderValidator {

    // 유저와 주문 유저 일치 여부 검사
    public void validateUserOwnsOrder(String orderUserId, String userId) {
        if (!orderUserId.equals(userId)) {
            throw new BusinessException(ExceptionMessage.NOT_AUTHORIZED);
        }
    }
}
