package radiata.service.brand.core.service.FeignClient;

import java.util.List;
import radiata.service.brand.core.model.TimeSaleProductGetResponseDto;

//@FeignClient(name = "timeSale")
public interface TimeSaleClient {

    List<TimeSaleProductGetResponseDto> getProducts();

}
