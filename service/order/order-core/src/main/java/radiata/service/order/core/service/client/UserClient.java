package radiata.service.order.core.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import radiata.common.response.CommonResponse;

@FeignClient(name = "user-service")
public interface UserClient {

    // TODO - uri 뒤에 /{userId} 붙이는게 RestFul 원칙?
    @GetMapping("/users")
    CommonResponse getUserInfo(String userId);
}
