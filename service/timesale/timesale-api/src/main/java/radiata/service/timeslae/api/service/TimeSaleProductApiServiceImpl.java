package radiata.service.timeslae.api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import radiata.common.domain.timesale.dto.response.TimeSaleProductCreateRequestDto;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;
import radiata.service.timesale.core.component.DistributeLockExecutor;
import radiata.service.timesale.core.service.interfaces.TimeSaleProductService;

@Service
@RequiredArgsConstructor
public class TimeSaleProductApiServiceImpl implements TimeSaleProductApiService {

    private final TimeSaleProductService timeSaleProductService;
    private final DistributeLockExecutor distributeLockExecutor;

    @Override
    public TimeSaleProductResponseDto createTimeSaleProduct(TimeSaleProductCreateRequestDto requestDto) {

        return timeSaleProductService.createTimeSaleProduct(requestDto);
    }

    @Override
    public void saleTimeSaleProduct(String timeSaleProductId) {

        distributeLockExecutor.execute("lock_" + timeSaleProductId, 10000, 10000, () -> {
            timeSaleProductService.sale(timeSaleProductId);
        });
    }

    @Override
    public List<TimeSaleProductResponseDto> getMaxDiscountTimeSaleProduct(List<String> productIds) {

        return timeSaleProductService.getMaxDiscountTimeSaleProduct(productIds);
    }

    @Override
    public List<TimeSaleProductResponseDto> getMaxDiscountTimeSaleProductHasStock(List<String> productIds) {

        return timeSaleProductService.getMaxDiscountTimeSaleProductHasStock(productIds);
    }
}
