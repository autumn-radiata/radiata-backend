package radiata.service.timesale.core.service.mapper;

import radiata.common.domain.timesale.dto.response.TimeSaleProductCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;
import radiata.service.timesale.core.domain.TimeSaleProduct;

public interface TimeSaleProductMapper {

    TimeSaleProduct toEntity(TimeSaleProductCreateRequestDto requestDto, String id);

    TimeSaleProductResponseDto toDto(TimeSaleProduct timeSaleProduct);
}
