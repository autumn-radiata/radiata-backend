package radiata.service.order.core.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;
import radiata.common.response.SuccessResponse;

@FeignClient(name = "coupon-service")
public interface CouponIssueClient {

    @PatchMapping("/couponissues/{couponIssueId}")
    SuccessResponse<CouponIssueResponseDto> useCouponIssue(@PathVariable String couponIssueId,
        @RequestHeader("X-UserId") String userId);

    @GetMapping("/coupons/{couponId}")
    SuccessResponse<CouponResponseDto> getCouponType(@PathVariable String couponId);
}
