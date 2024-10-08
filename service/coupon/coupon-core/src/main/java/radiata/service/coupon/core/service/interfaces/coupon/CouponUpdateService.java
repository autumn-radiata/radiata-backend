package radiata.service.coupon.core.service.interfaces.coupon;

import radiata.common.domain.coupon.dto.request.CouponUpdateRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;

public interface CouponUpdateService {

    CouponResponseDto updateCoupon(String couponId, CouponUpdateRequestDto requestDto);

}
