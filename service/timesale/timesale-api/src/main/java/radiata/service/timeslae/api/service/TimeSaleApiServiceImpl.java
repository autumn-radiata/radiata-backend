package radiata.service.timeslae.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import radiata.common.domain.timesale.dto.condition.TimeSaleSearchCondition;
import radiata.common.domain.timesale.dto.request.TimeSaleCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleResponseDto;
import radiata.service.timesale.core.service.interfaces.TimeSaleService;

@Service
@RequiredArgsConstructor
public class TimeSaleApiServiceImpl implements TimeSaleApiService {

    private final TimeSaleService timeSaleService;

    @Override
    public TimeSaleResponseDto createTimeSale(TimeSaleCreateRequestDto requestDto) {

        return timeSaleService.createTimeSale(requestDto);
    }

    @Override
    public Page<TimeSaleResponseDto> getTimeSales(TimeSaleSearchCondition condition, Pageable pageable) {

        return timeSaleService.getTimeSales(condition, pageable);
    }

    @Override
    public TimeSaleResponseDto getTimeSale(String timeSaleId) {

        return timeSaleService.getTimeSale(timeSaleId);
    }
}
