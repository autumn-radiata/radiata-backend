package radiata.service.coupon.core.implementation.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.coupon.dto.condition.CouponSearchCondition;
import radiata.service.coupon.core.domain.model.Coupon;

public interface CouponReader {

    Coupon readCoupon(String couponId);

    Page<Coupon> readCouponsByCondition(CouponSearchCondition condition, Pageable pageable);
}
