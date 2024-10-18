package radiata.service.coupon.core.implementation.interfaces;

import radiata.service.coupon.core.domain.model.CouponIssue;

public interface CouponIssueSaver {

    CouponIssue save(CouponIssue couponIssue);

    void saveToRedis(String couponId, String userId, Integer totalQuantity);
}
