package radiata.service.coupon.core.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.repository.CouponRepository;

public interface CouponJpaRepository extends JpaRepository<Coupon, String>, CouponRepository {

}
