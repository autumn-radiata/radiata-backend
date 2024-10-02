package radiata.service.coupon.core.domain.repository;

import radiata.service.coupon.core.domain.model.CouponIssue;

public interface CouponIssueQueryRepository {

    CouponIssue findFirstCouponIssue(String couponId, String userId);
}
