package radiata.service.timesale.core.implementation;

import static radiata.common.message.ExceptionMessage.NOT_FOUND;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.annotation.Implementation;
import radiata.common.domain.timesale.dto.condition.TimeSaleSearchCondition;
import radiata.common.exception.BusinessException;
import radiata.service.timesale.core.domain.TimeSale;
import radiata.service.timesale.core.domain.repository.TimeSaleQueryRepository;
import radiata.service.timesale.core.domain.repository.TimeSaleRepository;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleReader;

@Implementation
@RequiredArgsConstructor
public class TimeSaleReaderImpl implements TimeSaleReader {

    private final TimeSaleRepository timeSaleRepository;
    private final TimeSaleQueryRepository timeSaleQueryRepository;

    @Override
    public TimeSale read(String timeSaleId) {

        return timeSaleRepository.findById(timeSaleId).orElseThrow(
            () -> new BusinessException(NOT_FOUND)
        );
    }

    @Override
    public Page<TimeSale> readByCondition(TimeSaleSearchCondition condition, Pageable pageable) {

        return timeSaleQueryRepository.findTimeSalesByCondition(condition, pageable);
    }

    @Override
    public TimeSale readByProductId(String productId) {

        return timeSaleQueryRepository.findByProductId(productId).orElseThrow(
            () -> new BusinessException(NOT_FOUND)
        );
    }

    @Override
    public List<TimeSale> readByProductIds(List<String> productIds) {

        return timeSaleQueryRepository.findByProductIds(productIds);
    }
}
