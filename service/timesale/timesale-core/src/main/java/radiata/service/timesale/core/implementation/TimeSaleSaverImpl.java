package radiata.service.timesale.core.implementation;

import lombok.RequiredArgsConstructor;
import radiata.common.annotation.Implementation;
import radiata.service.timesale.core.domain.TimeSale;
import radiata.service.timesale.core.domain.repository.TimeSaleRepository;
import radiata.service.timesale.core.implementation.interfaces.TimeSaleSaver;

@Implementation
@RequiredArgsConstructor
public class TimeSaleSaverImpl implements TimeSaleSaver {

    private final TimeSaleRepository timeSaleRepository;

    @Override
    public TimeSale save(TimeSale timeSale) {

        return timeSaleRepository.save(timeSale);
    }
}
