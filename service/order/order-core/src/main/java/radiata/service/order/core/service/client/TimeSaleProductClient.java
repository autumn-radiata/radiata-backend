package radiata.service.order.core.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import radiata.common.domain.timesale.dto.request.TimeSaleProductSaleRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;
import radiata.common.response.SuccessResponse;

@FeignClient(name = "timesale-service")
public interface TimeSaleProductClient {

    @PatchMapping("/timesale-products/{timeSaleProductId}")
    SuccessResponse<TimeSaleProductResponseDto> checkAndDeductTimeSaleProduct(@PathVariable String timeSaleProductId,
        @RequestBody TimeSaleProductSaleRequestDto requestDto);
}
