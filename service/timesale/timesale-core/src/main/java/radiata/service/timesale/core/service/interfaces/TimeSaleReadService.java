package radiata.service.timesale.core.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.timesale.dto.condition.TimeSaleSearchCondition;
import radiata.common.domain.timesale.dto.response.TimeSaleResponseDto;

public interface TimeSaleReadService {

    TimeSaleResponseDto getTimeSale(String timeSaleId);

    Page<TimeSaleResponseDto> getTimeSales(TimeSaleSearchCondition timeSaleSearchCondition, Pageable pageable);
}
