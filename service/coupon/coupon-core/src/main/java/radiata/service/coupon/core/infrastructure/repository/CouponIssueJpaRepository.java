package radiata.service.coupon.core.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import radiata.service.coupon.core.domain.model.CouponIssue;
import radiata.service.coupon.core.domain.repository.CouponIssueRepository;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, String>, CouponIssueRepository {

}
