package radiata.service.coupon.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.request.CouponUpdateRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.implementation.interfaces.CouponIdCreator;
import radiata.service.coupon.core.implementation.interfaces.CouponSaver;
import radiata.service.coupon.core.service.interfaces.CouponService;
import radiata.service.coupon.core.service.mapper.CouponMapper;

@Service
@RequiredArgsConstructor
public class CouponServiceImplV1 implements CouponService {

    private final CouponMapper couponMapper;
    private final CouponIdCreator couponIdCreator;
    private final CouponSaver couponSaver;

    @Override
    public CouponResponseDto createCoupon(CouponCreateRequestDto requestDto) {

        String couponId = couponIdCreator.create();

        Coupon savedCoupon = couponSaver.save(couponMapper.toEntity(requestDto, couponId));

        return couponMapper.toDto(savedCoupon);
    }

    @Override
    public CouponResponseDto deleteCoupon(String couponId) {
        return null;
    }

    @Override
    public Page<CouponResponseDto> getCoupons() {
        return null;
    }

    @Override
    public CouponResponseDto getCoupon(String couponId) {
        return null;
    }

    @Override
    public CouponResponseDto updateCoupon(String couponId, CouponUpdateRequestDto requestDto) {
        return null;
    }
}
