package radiata.service.coupon.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.coupon.dto.condition.CouponSearchCondition;
import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.request.CouponUpdateRequestDto;
import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;

public interface CouponApiService {

    CouponResponseDto createCoupon(CouponCreateRequestDto requestDto);

    Page<CouponResponseDto> getCoupons(CouponSearchCondition condition, Pageable pageable);

    CouponResponseDto getCoupon(String couponId);

    CouponResponseDto updateCoupon(String couponId, CouponUpdateRequestDto requestDto);

    void deleteCoupon(String couponId);

    CouponIssueResponseDto issueCoupon(String couponId, String userId);

    CouponIssueResponseDto useCouponIssue(String couponIssueId, String userId);

    CouponIssueResponseDto getCouponIssue(String couponIssueId, String userId);

    Page<CouponIssueResponseDto> getCouponIssues(String userId, Pageable pageable);

    void deleteCouponIssue(String couponIssueId, String userId);
}
