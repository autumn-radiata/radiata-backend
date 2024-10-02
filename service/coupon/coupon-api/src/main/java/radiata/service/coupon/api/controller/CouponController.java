package radiata.service.coupon.api.controller;

import static radiata.common.message.SuccessMessage.CREATE_COUPON;
import static radiata.common.response.SuccessResponse.success;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.response.CommonResponse;
import radiata.service.coupon.api.service.CouponApiService;

@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponApiService couponApiService;

    @PostMapping("/api/coupons")
    public ResponseEntity<? extends CommonResponse> createCoupon(
        @Valid @RequestBody
        CouponCreateRequestDto requestDto
    ) {

        return ResponseEntity.status(CREATE_COUPON.getHttpStatus())
            .body(success(CREATE_COUPON.getMessage(), couponApiService.createCoupon(requestDto)));
    }

}
