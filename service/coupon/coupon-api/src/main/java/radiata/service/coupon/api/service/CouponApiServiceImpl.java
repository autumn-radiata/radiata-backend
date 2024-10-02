package radiata.service.coupon.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;
import radiata.service.coupon.core.service.interfaces.CouponService;

@Service
@RequiredArgsConstructor
public class CouponApiServiceImpl implements CouponApiService {

    private final CouponService couponService;

    @Override
    public CouponResponseDto createCoupon(CouponCreateRequestDto requestDto) {

        return couponService.createCoupon(requestDto);
    }
}
