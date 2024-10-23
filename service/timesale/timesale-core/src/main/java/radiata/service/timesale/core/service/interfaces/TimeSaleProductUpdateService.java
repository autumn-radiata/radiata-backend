package radiata.service.timesale.core.service.interfaces;

import radiata.common.domain.timesale.dto.request.TimeSaleProductSaleRequestDto;

public interface TimeSaleProductUpdateService {

    void sale(String timeSaleProductId, TimeSaleProductSaleRequestDto requestDto);

}
