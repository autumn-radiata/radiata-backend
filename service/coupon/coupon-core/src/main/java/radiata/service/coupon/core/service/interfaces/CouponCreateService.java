package radiata.service.coupon.core.service.interfaces;

import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;

public interface CouponCreateService {

    CouponResponseDto createCoupon(CouponCreateRequestDto requestDto);

}
