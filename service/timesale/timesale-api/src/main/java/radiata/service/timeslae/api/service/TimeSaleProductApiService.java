package radiata.service.timeslae.api.service;

import java.util.List;
import radiata.common.domain.timesale.dto.request.TimeSaleProductSaleRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleProductCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;

public interface TimeSaleProductApiService {

    TimeSaleProductResponseDto createTimeSaleProduct(TimeSaleProductCreateRequestDto requestDto);

    void saleTimeSaleProduct(String timeSaleProductId, TimeSaleProductSaleRequestDto requestDto);

    List<TimeSaleProductResponseDto> getMaxDiscountTimeSaleProduct(List<String> productIds);

    List<TimeSaleProductResponseDto> getMaxDiscountTimeSaleProductHasStock(List<String> productIds);
}