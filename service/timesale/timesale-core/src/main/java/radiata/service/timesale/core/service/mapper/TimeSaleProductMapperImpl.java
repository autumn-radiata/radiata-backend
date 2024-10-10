package radiata.service.timesale.core.service.mapper;

import org.springframework.stereotype.Component;
import radiata.common.domain.timesale.dto.response.TimeSaleProductCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;
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

    @Override
    public TimeSaleProductResponseDto toDto(TimeSaleProduct timeSaleProduct) {

        return TimeSaleProductResponseDto.builder()
            .timeSaleProductId(timeSaleProduct.getId())
            .productId(timeSaleProduct.getProductId())
            .saleQuantity(timeSaleProduct.getSaleQuantity())
            .totalQuantity(timeSaleProduct.getTotalQuantity())
            .discountRate(timeSaleProduct.getDiscountRate())
            .timeSaleStartTime(timeSaleProduct.getTimeSaleStartTime())
            .timeSaleEndTime(timeSaleProduct.getTimeSaleEndTime())
            .build();
    }
}
