package radiata.service.timesale.core.service.interfaces;

import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;

public interface TimeSaleProductReadService {

    TimeSaleProductResponseDto getMaxDiscountTimeSaleProduct(String productId);
}
