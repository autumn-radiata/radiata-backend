package radiata.service.coupon.core.service.interfaces;

import radiata.common.domain.coupon.dto.response.CouponResponseDto;

public interface CouponDeleteService {

    CouponResponseDto deleteCoupon(String couponId);

}
