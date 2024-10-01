package radiata.service.coupon.core.service.interfaces;

import org.springframework.data.domain.Page;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;

public interface CouponReadService {

    Page<CouponResponseDto> getCoupons();

    CouponResponseDto getCoupon(String couponId);
}
