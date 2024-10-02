package radiata.service.coupon.api.controller;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static radiata.common.message.SuccessMessage.*;
import static radiata.common.message.SuccessMessage.CREATE_COUPON;
import static radiata.common.message.SuccessMessage.DELETE_COUPON;
import static radiata.common.message.SuccessMessage.GET_COUPON;
import static radiata.common.message.SuccessMessage.GET_COUPONS;
import static radiata.common.message.SuccessMessage.ISSUE_COUPON;
import static radiata.common.message.SuccessMessage.UPDATE_COUPON;
import static radiata.common.response.SuccessResponse.success;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.domain.coupon.dto.condition.CouponSearchCondition;
import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.request.CouponUpdateRequestDto;
import radiata.common.response.CommonResponse;
import radiata.service.coupon.api.service.CouponApiService;

@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponApiService couponApiService;

    @PostMapping("/coupons")
    public ResponseEntity<? extends CommonResponse> createCoupon(
        @Valid @RequestBody
        CouponCreateRequestDto requestDto
    ) {

        return ResponseEntity.status(CREATE_COUPON.getHttpStatus())
            .body(success(CREATE_COUPON.getMessage(), couponApiService.createCoupon(requestDto)));
    }

    @GetMapping("/coupons")
    public ResponseEntity<? extends CommonResponse> getCoupons(
        @ModelAttribute
        CouponSearchCondition condition,
        @PageableDefault(size = 10, sort = "issueEndDate", direction = ASC)
        Pageable pageable
    ) {

        return ResponseEntity.status(GET_COUPONS.getHttpStatus())
            .body(success(GET_COUPONS.getMessage(), couponApiService.getCoupons(condition, pageable)));
    }

    @GetMapping("/coupons/{couponId}")
    public ResponseEntity<? extends CommonResponse> getCoupons(
        @PathVariable
        String couponId
    ) {

        return ResponseEntity.status(GET_COUPON.getHttpStatus())
            .body(success(GET_COUPON.getMessage(), couponApiService.getCoupon(couponId)));
    }

    @PatchMapping("/coupons/{couponId}")
    public ResponseEntity<? extends CommonResponse> updateCoupon(
        @PathVariable
        String couponId,
        @RequestBody
        CouponUpdateRequestDto requestDto
    ) {

        return ResponseEntity.status(UPDATE_COUPON.getHttpStatus())
            .body(success(UPDATE_COUPON.getMessage(), couponApiService.updateCoupon(couponId, requestDto)));
    }

    @DeleteMapping("/coupons/{couponId}")
    public ResponseEntity<? extends CommonResponse> deleteCoupon(
        @PathVariable
        String couponId
    ) {

        couponApiService.deleteCoupon(couponId);

        return ResponseEntity.status(DELETE_COUPON.getHttpStatus())
            .body(success(DELETE_COUPON.getMessage()));
    }

    @PostMapping("/coupons/{couponId}/issue")
    public ResponseEntity<? extends CommonResponse> issueCoupon(
        @PathVariable
        String couponId,
        @RequestHeader("X-UserId")
        String userId
    ) {

        couponApiService.issueCoupon(couponId, userId);

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
            .body(success(USE_COUPON_ISSUE.getMessage(), couponApiService.useCouponIssue(couponIssueId, userId)));
    }

    @GetMapping("/couponissues/{couponIssueId}")
    public ResponseEntity<? extends CommonResponse> getCouponIssue(
        @PathVariable
        String couponIssueId,
        @RequestHeader("X-UserId")
        String userId
    ) {

        return ResponseEntity.status(GET_COUPON_ISSUE.getHttpStatus())
            .body(success(GET_COUPON_ISSUE.getMessage(), couponApiService.getCouponIssue(couponIssueId, userId)));
    }

    @GetMapping("/couponissues")
    public ResponseEntity<? extends CommonResponse> getCouponIssues(
        @RequestHeader("X-UserId")
        String userId,
        @PageableDefault(size = 10, sort = "createdAt", direction = DESC)
        Pageable pageable
    ) {

        return ResponseEntity.status(GET_COUPON_ISSUES.getHttpStatus())
            .body(success(GET_COUPON_ISSUES.getMessage(), couponApiService.getCouponIssues(userId, pageable)));
    }

    @DeleteMapping("/couponissues/{couponIssueId}")
    public ResponseEntity<? extends CommonResponse> deleteCouponIssue(
        @PathVariable
        String couponIssueId,
        @RequestHeader("X-UserId")
        String userId
    ) {

        couponApiService.deleteCouponIssue(couponIssueId, userId);

        return ResponseEntity.status(DELETE_COUPON_ISSUES.getHttpStatus())
            .body(success(DELETE_COUPON_ISSUES.getMessage()));
    }

}
