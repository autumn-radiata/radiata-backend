package radiata.service.coupon.core.implementation;

import lombok.RequiredArgsConstructor;
import radiata.common.annotation.Implementation;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.repository.CouponRepository;
import radiata.service.coupon.core.implementation.interfaces.CouponDeleter;

@Implementation
@RequiredArgsConstructor
public class CouponDeleterImpl implements CouponDeleter {

    private final CouponRepository couponRepository;

    @Override
    public void deleteCoupon(Coupon coupon) {

        couponRepository.delete(coupon);
    }
}
