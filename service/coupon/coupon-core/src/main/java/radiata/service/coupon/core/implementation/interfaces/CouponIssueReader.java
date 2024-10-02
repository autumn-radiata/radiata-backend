package radiata.service.coupon.core.implementation.interfaces;

import radiata.service.coupon.core.domain.model.CouponIssue;

public interface CouponIssueReader {

    CouponIssue readCouponIssue(String couponIssueId);

    CouponIssue readFirstCouponIssue(String couponId, String userId);
}
