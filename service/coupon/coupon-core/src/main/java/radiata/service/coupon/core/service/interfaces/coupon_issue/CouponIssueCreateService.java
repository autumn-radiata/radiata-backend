package radiata.service.coupon.core.service.interfaces.coupon_issue;

public interface CouponIssueCreateService {

    void issue(String couponId, String userId);

    void saveCouponIssue(String couponId, String userId);
}
