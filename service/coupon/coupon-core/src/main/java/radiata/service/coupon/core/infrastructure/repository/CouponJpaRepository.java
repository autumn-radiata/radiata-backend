package radiata.service.coupon.core.infrastructure.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.hibernate.annotations.OptimisticLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.repository.CouponRepository;

public interface CouponJpaRepository extends JpaRepository<Coupon, String>, CouponRepository {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT cp "
            + "FROM Coupon cp "
            + "WHERE cp.id = :couponId")
    Optional<Coupon> findByIdWithLock(String couponId);
}
