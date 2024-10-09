package radiata.service.timesale.core.service.interfaces;

import radiata.common.domain.timesale.dto.request.TimeSaleCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleResponseDto;

public interface TimeSaleCreateService {

    TimeSaleResponseDto createTimeSale(TimeSaleCreateRequestDto requestDto);
}
