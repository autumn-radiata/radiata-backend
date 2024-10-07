package radiata.service.timesale.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import radiata.common.domain.timesale.dto.condition.TimeSaleSearchCondition;
import radiata.common.domain.timesale.dto.request.TimeSaleCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleResponseDto;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleCreateRequestDtoValidator;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleIdCreator;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleReader;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleSaver;
import radiata.service.timesale.core.service.interfaces.TimeSaleService;
import radiata.service.timesale.core.service.mapper.TimeSaleMapper;


@Service
@RequiredArgsConstructor
public class TimeSaleServiceImpl implements TimeSaleService {

    private final TimeSaleMapper timeSaleMapper;
    private final TimeSaleIdCreator timeSaleIdCreator;
    private final TimeSaleSaver timeSaleSaver;
    private final TimeSaleCreateRequestDtoValidator timeSaleCreateRequestDtoValidator;
    private final TimeSaleReader timeSaleReader;

    @Override
    public TimeSaleResponseDto createTimeSale(TimeSaleCreateRequestDto requestDto) {

        timeSaleCreateRequestDtoValidator.validate(requestDto);

        return timeSaleMapper.toDto(
            timeSaleSaver.save(timeSaleMapper.toEntity(requestDto, timeSaleIdCreator.create())));
    }

    @Override
    public TimeSaleResponseDto getTimeSale(String timeSaleId) {

        return timeSaleMapper.toDto(timeSaleReader.read(timeSaleId));
    }

    @Override
    public Page<TimeSaleResponseDto> getTimeSales(TimeSaleSearchCondition timeSaleSearchCondition, Pageable pageable) {

        return timeSaleReader.readByCondition(timeSaleSearchCondition, pageable).map(timeSaleMapper::toDto);
    }
}
