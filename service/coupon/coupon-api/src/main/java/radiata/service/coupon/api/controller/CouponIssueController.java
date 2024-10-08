package radiata.service.coupon.api.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static radiata.common.message.SuccessMessage.DELETE_COUPON_ISSUES;
import static radiata.common.message.SuccessMessage.GET_COUPON_ISSUE;
import static radiata.common.message.SuccessMessage.GET_COUPON_ISSUES;
import static radiata.common.message.SuccessMessage.ISSUE_COUPON;
import static radiata.common.message.SuccessMessage.USE_COUPON_ISSUE;
import static radiata.common.response.SuccessResponse.success;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.response.CommonResponse;
import radiata.service.coupon.api.service.CouponIssueApiService;

@RestController
@RequiredArgsConstructor
public class CouponIssueController {

    private final CouponIssueApiService couponIssueApiService;

    @PostMapping("/coupons/{couponId}/issue")
    public ResponseEntity<? extends CommonResponse> issueCoupon(
        @PathVariable
        String couponId,
        @RequestHeader("X-UserId")
        String userId
    ) {

        couponIssueApiService.issueCoupon(couponId, userId);

        return ResponseEntity.status(ISSUE_COUPON.getHttpStatus())
            .body(success(ISSUE_COUPON.getMessage()));
    }

    @PatchMapping("/couponissues/{couponIssueId}")
    public ResponseEntity<? extends CommonResponse> useIssueCoupon(
        @PathVariable
        String couponIssueId,
        @RequestHeader("X-UserId")
        String userId
    ) {

        return ResponseEntity.status(USE_COUPON_ISSUE.getHttpStatus())
            .body(success(USE_COUPON_ISSUE.getMessage(), couponIssueApiService.useCouponIssue(couponIssueId, userId)));
    }

    @GetMapping("/couponissues/{couponIssueId}")
    public ResponseEntity<? extends CommonResponse> getCouponIssue(
        @PathVariable
        String couponIssueId,
        @RequestHeader("X-UserId")
        String userId
    ) {

        return ResponseEntity.status(GET_COUPON_ISSUE.getHttpStatus())
            .body(success(GET_COUPON_ISSUE.getMessage(), couponIssueApiService.getCouponIssue(couponIssueId, userId)));
    }

    @GetMapping("/couponissues")
    public ResponseEntity<? extends CommonResponse> getCouponIssues(
        @RequestHeader("X-UserId")
        String userId,
        @PageableDefault(size = 10, sort = "createdAt", direction = DESC)
        Pageable pageable
    ) {

        return ResponseEntity.status(GET_COUPON_ISSUES.getHttpStatus())
            .body(success(GET_COUPON_ISSUES.getMessage(), couponIssueApiService.getCouponIssues(userId, pageable)));
    }

    @DeleteMapping("/couponissues/{couponIssueId}")
    public ResponseEntity<? extends CommonResponse> deleteCouponIssue(
        @PathVariable
        String couponIssueId,
        @RequestHeader("X-UserId")
        String userId
    ) {

        couponIssueApiService.deleteCouponIssue(couponIssueId, userId);

        return ResponseEntity.status(DELETE_COUPON_ISSUES.getHttpStatus())
            .body(success(DELETE_COUPON_ISSUES.getMessage()));
    }

}
