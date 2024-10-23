package radiata.common.domain.coupon.dto.request;

import radiata.common.domain.coupon.constant.CouponStatus;

public record CouponIssueRollbackRequestDto(
        String couponIssueId,
        CouponStatus couponStatus
) {

}
