package radiata.service.order.core.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import radiata.common.response.SuccessResponse;

@FeignClient(name = "timesale-service")
public interface TimeSaleProductClient {

    @PatchMapping("/timesale-products/{timeSaleProductId}")
    SuccessResponse<String> checkAndDeductTimeSaleProduct(@PathVariable String timeSaleProductId);
}
