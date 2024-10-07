package radiata.service.timeslae.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.timesale.dto.condition.TimeSaleSearchCondition;
import radiata.common.domain.timesale.dto.request.TimeSaleCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleResponseDto;

public interface TimeSaleApiService {

    TimeSaleResponseDto createTimeSale(TimeSaleCreateRequestDto requestDto);

    Page<TimeSaleResponseDto> getTimeSales(TimeSaleSearchCondition condition, Pageable pageable);

    TimeSaleResponseDto getTimeSale(String timeSaleId);

}
