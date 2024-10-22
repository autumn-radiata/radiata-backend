package radiata.service.coupon.core.util;

import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;

public enum CouponIssueRequestCode {
    SUCCESS(1),
    DUPLICATED_COUPON_ISSUE(2),
    INVALID_COUPON_ISSUE_QUANTITY(3);

    CouponIssueRequestCode(int code) {

    }

    public static CouponIssueRequestCode fromCode(String code) {
        int codeValue = Integer.parseInt(code);
        if (codeValue == 1) {
            return SUCCESS;
        }
        if (codeValue == 2) {
            return DUPLICATED_COUPON_ISSUE;
        }
        if (codeValue == 3) {
            return INVALID_COUPON_ISSUE_QUANTITY;
        }
        throw new BusinessException(ExceptionMessage.NOT_FOUND);
    }

    public static void checkRequestResult(CouponIssueRequestCode code) {
        if (code == INVALID_COUPON_ISSUE_QUANTITY) {
            throw new BusinessException(ExceptionMessage.COUPON_ISSUE_QUANTITY_LIMITED);
        }

        if (code == DUPLICATED_COUPON_ISSUE) {
            throw new BusinessException(ExceptionMessage.DUPLICATED_COUPON_ISSUE);
        }
    }
}
