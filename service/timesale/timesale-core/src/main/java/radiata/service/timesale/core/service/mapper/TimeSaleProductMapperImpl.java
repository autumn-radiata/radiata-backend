package radiata.service.timesale.core.service.mapper;

import org.springframework.stereotype.Component;
import radiata.common.domain.timesale.dto.response.TimeSaleProductCreateRequestDto;
import radiata.service.timesale.core.domain.TimeSaleProduct;

@Component
public class TimeSaleProductMapperImpl implements TimeSaleProductMapper {

    @Override
    public TimeSaleProduct toEntity(TimeSaleProductCreateRequestDto requestDto, String id) {

        return TimeSaleProduct.of(
            id,
            requestDto.productId(),
            requestDto.discountRate(),
            0,
            requestDto.totalQuantity(),
            requestDto.timeSaleStartTime(),
            requestDto.timeSaleEndTime()
        );
    }
}
