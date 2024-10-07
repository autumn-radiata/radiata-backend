package radiata.service.timeslae.api.service;

import radiata.common.domain.timesale.dto.request.TimeSaleCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleResponseDto;

public interface TimeSaleApiService {

    TimeSaleResponseDto createTimeSale(TimeSaleCreateRequestDto requestDto);
}
