package radiata.service.coupon.core.implementation.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.service.coupon.core.domain.model.CouponIssue;

public interface CouponIssueReader {

    CouponIssue readCouponIssue(String couponIssueId);

    CouponIssue readFirstCouponIssue(String couponId, String userId);

    Page<CouponIssue> readCouponIssuesByUserId(String userId, Pageable pageable);
}
