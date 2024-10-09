package radiata.service.order.core.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import radiata.common.response.SuccessResponse;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products/{productId}")
        // TODO - 타입 변경 예정(String -> ProductResponseDto?)
    SuccessResponse<String> getProductInfo(@PathVariable("productId") String productId);

}
