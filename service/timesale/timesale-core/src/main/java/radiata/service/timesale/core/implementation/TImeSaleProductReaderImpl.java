package radiata.service.timesale.core.implementation;

import static radiata.common.message.ExceptionMessage.NOT_FOUND;

import lombok.RequiredArgsConstructor;
import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.service.timesale.core.domain.TimeSaleProduct;
import radiata.service.timesale.core.domain.repository.TimeSaleProductRepository;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleProductReader;

@Implementation
@RequiredArgsConstructor
public class TImeSaleProductReaderImpl implements TimeSaleProductReader {

    private final TimeSaleProductRepository timeSaleProductRepository;

    @Override
    public TimeSaleProduct read(String timeSaleProductId) {

        return timeSaleProductRepository.findById(timeSaleProductId).orElseThrow(
            () -> new BusinessException(NOT_FOUND)
        );
    }
}
