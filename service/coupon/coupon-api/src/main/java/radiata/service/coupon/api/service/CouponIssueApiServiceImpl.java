package radiata.service.coupon.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;
import radiata.service.coupon.core.service.interfaces.coupon_issue.CouponIssueService;

@RequiredArgsConstructor
@Service
public class CouponIssueApiServiceImpl implements CouponIssueApiService {

    private final CouponIssueService couponIssueService;

    @Override
    public void issueCoupon(String couponId, String userId) {

        couponIssueService.issue(couponId, userId);
    }

    @Override
    public CouponIssueResponseDto useCouponIssue(String couponIssueId, String userId) {

        return couponIssueService.useCouponIssue(couponIssueId, userId);
    }

    @Override
    public CouponIssueResponseDto getCouponIssue(String couponIssueId, String userId) {

        return couponIssueService.getCouponIssue(couponIssueId, userId);
    }

    @Override
    public Page<CouponIssueResponseDto> getCouponIssues(String userId, Pageable pageable) {

        return couponIssueService.getCouponIssues(userId, pageable);
    }

    @Override
    public void deleteCouponIssue(String couponIssueId, String userId) {

        couponIssueService.deleteCouponIssue(couponIssueId, userId);
    }

}
