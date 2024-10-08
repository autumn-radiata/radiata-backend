package radiata.service.coupon.core.service.interfaces.coupon_issue;

import radiata.service.coupon.core.domain.model.CouponIssue;

public interface CouponIssueDeleteService {

    void deleteCouponIssue(String couponIssueId, String userId);
}
