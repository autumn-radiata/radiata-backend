package radiata.service.timesale.core.implementation;

import lombok.RequiredArgsConstructor;
import radiata.common.annotation.Implementation;
import radiata.service.timesale.core.domain.TimeSaleProduct;
import radiata.service.timesale.core.domain.repository.TimeSaleProductRepository;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleProductSaver;

@Implementation
@RequiredArgsConstructor
public class TimeSaleProductSaverImpl implements TimeSaleProductSaver {

    private final TimeSaleProductRepository timeSaleProductRepository;

    @Override
    public TimeSaleProduct save(TimeSaleProduct timeSaleProduct) {

        return timeSaleProductRepository.save(timeSaleProduct);
    }
}
