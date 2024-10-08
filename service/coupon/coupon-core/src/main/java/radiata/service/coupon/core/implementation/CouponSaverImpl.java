package radiata.service.coupon.core.implementation;

import lombok.RequiredArgsConstructor;
import radiata.common.annotation.Implementation;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.repository.CouponRepository;
import radiata.service.coupon.core.implementation.interfaces.CouponSaver;

@Implementation
@RequiredArgsConstructor
public class CouponSaverImpl implements CouponSaver {

    private final CouponRepository couponRepository;

    @Override
    public Coupon save(Coupon coupon) {

        return couponRepository.save(coupon);
    }
}
