package radiata.service.order.core.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import radiata.common.domain.brand.request.ProductDeductRequestDto;
import radiata.common.response.SuccessResponse;

@FeignClient(name = "brand-service")
public interface ProductClient {

    @PatchMapping("/products/deduct")
    SuccessResponse<String> checkAndDeductStock(@RequestBody ProductDeductRequestDto requestDto);

}
