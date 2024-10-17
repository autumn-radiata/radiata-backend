package radiata.service.coupon.core.implementation.interfaces;

import radiata.service.coupon.core.domain.model.Coupon;

public interface CouponIssueValidator {

    void validate(Coupon coupon, String userId);
}
