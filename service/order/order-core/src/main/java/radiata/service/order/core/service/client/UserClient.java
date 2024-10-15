package radiata.service.order.core.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import radiata.common.domain.user.dto.request.PointModifyRequestDto;
import radiata.common.response.CommonResponse;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/points/deduct")
    CommonResponse checkAndUseRewardPoint(@RequestBody PointModifyRequestDto requestDto);
}
