package radiata.service.timesale.core.service.interfaces;

import java.util.List;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;

public interface TimeSaleProductReadService {

    List<TimeSaleProductResponseDto> getMaxDiscountTimeSaleProduct(List<String> productIds);

    List<TimeSaleProductResponseDto> getMaxDiscountTimeSaleProductHasStock(List<String> productIds);
}
