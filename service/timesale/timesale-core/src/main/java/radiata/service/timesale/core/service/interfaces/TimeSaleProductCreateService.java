package radiata.service.timesale.core.service.interfaces;

import radiata.common.domain.timesale.dto.response.TimeSaleProductCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;

public interface TimeSaleProductCreateService {

    TimeSaleProductResponseDto createTimeSaleProduct(TimeSaleProductCreateRequestDto timeSaleProduct);

}
