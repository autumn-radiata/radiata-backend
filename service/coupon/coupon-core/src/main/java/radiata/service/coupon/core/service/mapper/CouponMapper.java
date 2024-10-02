package radiata.service.coupon.core.service.mapper;

import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.request.CouponUpdateRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;
import radiata.service.coupon.core.domain.model.Coupon;

public interface CouponMapper {

    Coupon toEntity(CouponCreateRequestDto requestDto, String couponId);

    CouponResponseDto toDto(Coupon coupon);

    void update(Coupon coupon, CouponUpdateRequestDto requestDto);
}
