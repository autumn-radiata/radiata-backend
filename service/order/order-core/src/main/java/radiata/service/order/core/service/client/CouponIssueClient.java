package radiata.service.order.core.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import radiata.common.response.CommonResponse;

@FeignClient(name = "coupon-service")
public interface CouponIssueClient {

    @GetMapping("/couponissues/{couponIssueId}")
    ResponseEntity<? extends CommonResponse> getCouponIssue(@PathVariable String couponIssueId,
        @RequestHeader("X-UserId") String userId);

    @PatchMapping("/couponissues/{couponIssueId}")
    ResponseEntity<? extends CommonResponse> useCouponIssue(@PathVariable String couponIssueId,
        @RequestHeader("X-UserId") String userId);
}
