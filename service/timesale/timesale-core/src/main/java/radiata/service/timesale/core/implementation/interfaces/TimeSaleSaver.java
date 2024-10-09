package radiata.service.timesale.core.implementation.interfaces;

import radiata.service.timesale.core.domain.TimeSale;

public interface TimeSaleSaver {

    TimeSale save(TimeSale timeSale);
}
