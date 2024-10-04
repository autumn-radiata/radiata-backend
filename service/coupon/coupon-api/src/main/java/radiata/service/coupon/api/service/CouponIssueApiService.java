package radiata.service.coupon.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;

public interface CouponIssueApiService {

    void issueCoupon(String couponId, String userId);

    CouponIssueResponseDto useCouponIssue(String couponIssueId, String userId);

    CouponIssueResponseDto getCouponIssue(String couponIssueId, String userId);

    Page<CouponIssueResponseDto> getCouponIssues(String userId, Pageable pageable);

    void deleteCouponIssue(String couponIssueId, String userId);

}
