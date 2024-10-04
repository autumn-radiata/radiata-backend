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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.domain.coupon.dto.condition.CouponSearchCondition;
import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.request.CouponUpdateRequestDto;
import radiata.common.response.CommonResponse;
import radiata.service.coupon.api.service.CouponApiService;

@RequestMapping("/coupons")
@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponApiService couponApiService;

    @PostMapping
    public ResponseEntity<? extends CommonResponse> createCoupon(
        @Valid @RequestBody
        CouponCreateRequestDto requestDto
    ) {

        return ResponseEntity.status(CREATE_COUPON.getHttpStatus())
            .body(success(CREATE_COUPON.getMessage(), couponApiService.createCoupon(requestDto)));
    }

    @GetMapping
    public ResponseEntity<? extends CommonResponse> getCoupons(
        @ModelAttribute
        CouponSearchCondition condition,
        @PageableDefault(size = 10, sort = "issueEndDate", direction = ASC)
        Pageable pageable
    ) {

        return ResponseEntity.status(GET_COUPONS.getHttpStatus())
            .body(success(GET_COUPONS.getMessage(), couponApiService.getCoupons(condition, pageable)));
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<? extends CommonResponse> getCoupons(
        @PathVariable
        String couponId
    ) {

        return ResponseEntity.status(GET_COUPON.getHttpStatus())
            .body(success(GET_COUPON.getMessage(), couponApiService.getCoupon(couponId)));
    }

    @PatchMapping("/{couponId}")
    public ResponseEntity<? extends CommonResponse> updateCoupon(
        @PathVariable
        String couponId,
        @RequestBody
        CouponUpdateRequestDto requestDto
    ) {

        return ResponseEntity.status(UPDATE_COUPON.getHttpStatus())
            .body(success(UPDATE_COUPON.getMessage(), couponApiService.updateCoupon(couponId, requestDto)));
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<? extends CommonResponse> deleteCoupon(
        @PathVariable
        String couponId
    ) {

        couponApiService.deleteCoupon(couponId);

        return ResponseEntity.status(DELETE_COUPON.getHttpStatus())
            .body(success(DELETE_COUPON.getMessage()));
    }

}
