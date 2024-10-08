package radiata.service.coupon.core.service.interfaces.coupon_issue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;

public interface CouponIssueReadService {

    CouponIssueResponseDto getCouponIssue(String couponIssueId, String userId);

    Page<CouponIssueResponseDto> getCouponIssues(String userId, Pageable pageable);

}
