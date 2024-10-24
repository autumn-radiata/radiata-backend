package radiata.service.brand.core.service.FeignClient;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;
import radiata.common.response.SuccessResponse;

@FeignClient(name = "timesale-service")
public interface TimeSaleClient {

    //상품 단건 타임세일 최처가 조회
    @GetMapping("/timesale-products/max-discount")
    SuccessResponse<List<TimeSaleProductResponseDto>> getMaxDiscountTimeSaleProducts(
        @RequestParam List<String> productIds);


}
