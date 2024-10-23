package radiata.service.timesale.core.service.interfaces;

import radiata.common.domain.timesale.dto.request.TimeSaleProductSaleRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;

public interface TimeSaleProductUpdateService {

    TimeSaleProductResponseDto sale(String timeSaleProductId, TimeSaleProductSaleRequestDto requestDto);

}
