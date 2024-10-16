package radiata.service.timeslae.api.service;

import radiata.common.domain.timesale.dto.response.TimeSaleProductCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;

public interface TimeSaleProductApiService {

    TimeSaleProductResponseDto createTimeSaleProduct(TimeSaleProductCreateRequestDto requestDto);

    void saleTimeSaleProduct(String timeSaleProductId);

    TimeSaleProductResponseDto getMaxDiscountTimeSaleProduct(String productId);

    TimeSaleProductResponseDto getMaxDiscountTimeSaleProductHasStock(String productId);
}