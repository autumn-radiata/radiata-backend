package radiata.service.timesale.core.service.mapper;

import org.springframework.stereotype.Component;
import radiata.common.domain.timesale.dto.request.TimeSaleCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleResponseDto;
import radiata.service.timesale.core.domain.TimeSale;

@Component
public class TimeSaleMapperImpl implements TimeSaleMapper {

    @Override
    public TimeSale toEntity(TimeSaleCreateRequestDto requestDto, String id) {

        return TimeSale.of(id, requestDto.title(), requestDto.timeSaleStartDate(), requestDto.timeSaleEndDate());
    }

    @Override
    public TimeSaleResponseDto toDto(TimeSale timeSale) {

        return TimeSaleResponseDto.of(timeSale.getId(), timeSale.getTitle(), timeSale.getTimeSaleStartDate(),
            timeSale.getTimeSaleEndDate());
    }
}
