package radiata.service.coupon.core.service.interfaces.coupon_issue;

import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;

public interface CouponIssueUpdateService {

    CouponIssueResponseDto useCouponIssue(String couponIssueId, String userId);
}
