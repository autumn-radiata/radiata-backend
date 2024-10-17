package radiata.service.brand.core.implement;

import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;

@Implementation
public class ProductValidator {

    public void checkPriceAboveDiscountedAmount(int price, int discountAmount) {
        if (price < discountAmount) {
            throw new BusinessException(ExceptionMessage.INVALID_DISCOUNT_AMOUNT);
        }
    }

}
