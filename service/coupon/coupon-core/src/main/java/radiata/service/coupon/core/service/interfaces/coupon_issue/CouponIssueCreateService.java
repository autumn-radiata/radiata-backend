package radiata.service.coupon.core.service.interfaces.coupon_issue;

import radiata.service.coupon.core.domain.model.CouponIssue;

public interface CouponIssueCreateService {

    void issue(String couponId, String userId);

    CouponIssue saveCouponIssue(String couponId, String userId);

}
