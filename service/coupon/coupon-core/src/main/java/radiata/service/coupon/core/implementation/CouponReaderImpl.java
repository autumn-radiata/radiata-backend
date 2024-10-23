package radiata.service.coupon.core.implementation;

import static radiata.common.message.ExceptionMessage.NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.annotation.Implementation;
import radiata.common.domain.coupon.dto.condition.CouponSearchCondition;
import radiata.common.exception.BusinessException;
import radiata.service.coupon.core.domain.repository.CouponQueryRepository;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.repository.CouponRepository;
import radiata.service.coupon.core.implementation.interfaces.CouponReader;

@Implementation
@RequiredArgsConstructor
public class CouponReaderImpl implements CouponReader {

    private final CouponRepository couponRepository;
    private final CouponQueryRepository couponQueryRepository;

    @Override
    @Cacheable(value = "couponEntity", key = "#couponId")
    public Coupon readCoupon(String couponId) {

        return couponRepository.findById(couponId).orElseThrow(
            () -> new BusinessException(NOT_FOUND)
        );
    }

    public Coupon readCouponByLock(String couponId) {

        return couponRepository.findByIdWithLock(couponId).orElseThrow(
                () -> new BusinessException(NOT_FOUND)
        );
    }

    @Override
    public Page<Coupon> readCouponsByCondition(CouponSearchCondition condition, Pageable pageable) {

        return couponQueryRepository.findCouponsByCondition(condition, pageable);
    }
}
