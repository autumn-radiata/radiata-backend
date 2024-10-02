package radiata.service.coupon.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import radiata.common.domain.coupon.dto.condition.CouponSearchCondition;
import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.request.CouponUpdateRequestDto;
import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;
import radiata.service.coupon.core.service.interfaces.coupon.CouponService;
import radiata.service.coupon.core.service.interfaces.coupon_issue.CouponIssueService;

@Service
@RequiredArgsConstructor
public class CouponApiServiceImpl implements CouponApiService {

    private final CouponService couponService;

    private final CouponIssueService couponIssueService;

    @Override
    public CouponResponseDto createCoupon(CouponCreateRequestDto requestDto) {

        return couponService.createCoupon(requestDto);
    }

    @Override
    public Page<CouponResponseDto> getCoupons(CouponSearchCondition condition, Pageable pageable) {

        return couponService.getCoupons(condition, pageable);
    }

    @Override
    public CouponResponseDto getCoupon(String couponId) {

        return couponService.getCoupon(couponId);
    }

    @Override
    public CouponResponseDto updateCoupon(String couponId, CouponUpdateRequestDto requestDto) {

        return couponService.updateCoupon(couponId, requestDto);
    }

    @Override
    public void deleteCoupon(String couponId) {

        couponService.deleteCoupon(couponId);
    }

    @Override
    public CouponIssueResponseDto issueCoupon(String couponId, String userId) {

        return couponIssueService.issue(couponId, userId);
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
}
