package radiata.service.coupon.core.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.coupon.dto.condition.CouponSearchCondition;
import radiata.service.coupon.core.domain.model.Coupon;

public interface CouponQueryRepository {

    Page<Coupon> findCouponsByCondition(CouponSearchCondition condition, Pageable pageable);

}
