package radiata.service.timesale.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import radiata.common.domain.timesale.dto.request.TimeSaleCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleResponseDto;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleIdCreator;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleSaver;
import radiata.service.timesale.core.service.interfaces.TimeSaleService;
import radiata.service.timesale.core.service.mapper.TimeSaleMapper;


@Service
@RequiredArgsConstructor
public class TimeSaleServiceImpl implements TimeSaleService {

    private final TimeSaleMapper timeSaleMapper;
    private final TimeSaleIdCreator timeSaleIdCreator;
    private final TimeSaleSaver timeSaleSaver;

    @Override
    public TimeSaleResponseDto createTimeSale(TimeSaleCreateRequestDto requestDto) {

        return timeSaleMapper.toDto(
            timeSaleSaver.save(timeSaleMapper.toEntity(requestDto, timeSaleIdCreator.create())));
    }
}
