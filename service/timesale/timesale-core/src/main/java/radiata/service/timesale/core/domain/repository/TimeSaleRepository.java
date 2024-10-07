package radiata.service.timesale.core.domain.repository;

import radiata.service.timesale.core.domain.TimeSale;

public interface TimeSaleRepository {

    TimeSale save(TimeSale timeSale);
}
