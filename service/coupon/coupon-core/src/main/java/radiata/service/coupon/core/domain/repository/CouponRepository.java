package radiata.service.coupon.core.domain.repository;

import java.util.List;
import java.util.Optional;
import radiata.service.coupon.core.domain.model.Coupon;

public interface CouponRepository {

    Coupon save(Coupon coupon);

    Optional<Coupon> findById(String couponId);

    void delete(Coupon coupon);

    Optional<Coupon> findByIdWithLock(String couponId);
}
