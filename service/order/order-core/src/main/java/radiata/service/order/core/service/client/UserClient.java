package radiata.service.order.core.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import radiata.common.domain.user.dto.request.PointModifyRequestDto;
import radiata.common.response.SuccessResponse;

@FeignClient(name = "user-service")
public interface UserClient {

    @PatchMapping("/users/points/deduct")
    SuccessResponse<Void> checkAndUseRewardPoint(@RequestBody PointModifyRequestDto requestDto);
}
