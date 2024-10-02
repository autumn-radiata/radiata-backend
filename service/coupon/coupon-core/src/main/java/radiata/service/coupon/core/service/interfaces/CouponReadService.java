package radiata.service.coupon.core.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.coupon.dto.condition.CouponSearchCondition;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;

public interface CouponReadService {

    Page<CouponResponseDto> getCoupons(CouponSearchCondition condition, Pageable pageable);

    CouponResponseDto getCoupon(String couponId);
}
