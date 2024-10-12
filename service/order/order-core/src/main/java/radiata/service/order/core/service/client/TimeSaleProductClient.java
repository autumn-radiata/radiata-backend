package radiata.service.order.core.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import radiata.common.response.CommonResponse;

@FeignClient(name = "timesale-service")
public interface TimeSaleProductClient {

    // TODO - 수정 가능성 O (미확정)
    @GetMapping("/timesale-products/{timeSaleProductId}")
    ResponseEntity<? extends CommonResponse> getTimeSaleProduct(@PathVariable String timeSaleProductId);
}
