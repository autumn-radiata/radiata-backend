package radiata.service.timesale.core.service.mapper;

import radiata.common.domain.timesale.dto.request.TimeSaleCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleResponseDto;
import radiata.service.timesale.core.domain.TimeSale;

public interface TimeSaleMapper {

    TimeSale toEntity(TimeSaleCreateRequestDto requestDto, String id);

    TimeSaleResponseDto toDto(TimeSale timeSale);
}
