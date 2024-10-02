package radiata.service.coupon.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.coupon.dto.condition.CouponSearchCondition;
import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;

public interface CouponApiService {

    CouponResponseDto createCoupon(CouponCreateRequestDto requestDto);

    Page<CouponResponseDto> getCoupons(CouponSearchCondition condition, Pageable pageable);
}
