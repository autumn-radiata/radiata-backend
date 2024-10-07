package radiata.service.timeslae.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
}
