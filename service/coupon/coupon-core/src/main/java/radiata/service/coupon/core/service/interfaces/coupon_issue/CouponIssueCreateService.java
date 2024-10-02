package radiata.service.coupon.core.service.interfaces.coupon_issue;

import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;
import radiata.service.coupon.core.domain.model.CouponIssue;

public interface CouponIssueCreateService {

    CouponIssueResponseDto issue(String couponId, String userId);

    CouponIssue saveCouponIssue(String couponId, String userId);

}
