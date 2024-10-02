package radiata.service.coupon.core.domain.repository;

import org.springframework.stereotype.Repository;
import radiata.service.coupon.core.domain.model.CouponIssue;

@Repository
public interface CouponIssueRepository {

    CouponIssue save(CouponIssue couponIssue);

}
