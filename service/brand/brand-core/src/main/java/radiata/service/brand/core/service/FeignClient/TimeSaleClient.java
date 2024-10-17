package radiata.service.brand.core.service.FeignClient;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;
import radiata.common.response.SuccessResponse;

@FeignClient(name = "timesale-service")
public interface TimeSaleClient {


    //상품 단건 타임세일 최처가 조회
    @GetMapping("/products/{productId}/timesale-products/max-discount")
    SuccessResponse<TimeSaleProductResponseDto> getMaxDiscountTimeSaleProduct(
        @PathVariable("productId") String productId);

    //상품 리스트 타임세일 최저가 조회
    @GetMapping("/products/itmesale-prducts/max-discount")
    SuccessResponse<List<TimeSaleProductResponseDto>> getMaxDiscountTimeSaleProducts();
}
