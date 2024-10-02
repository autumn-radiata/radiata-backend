package radiata.service.coupon.core.domain.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import radiata.service.coupon.core.domain.model.CouponIssue;

@Repository
public interface CouponIssueRepository {

    CouponIssue save(CouponIssue couponIssue);

    Optional<CouponIssue> findById(String couponIssueId);
}
