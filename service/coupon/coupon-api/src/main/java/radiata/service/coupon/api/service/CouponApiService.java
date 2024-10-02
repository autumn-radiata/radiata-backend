package radiata.service.coupon.api.service;

import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;

public interface CouponApiService {

    CouponResponseDto createCoupon(CouponCreateRequestDto requestDto);
}
