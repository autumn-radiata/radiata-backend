package radiata.service.timesale.core.domain.repository;

import radiata.service.timesale.core.domain.TimeSaleProduct;

public interface TimeSaleProductRepository {

    TimeSaleProduct save(TimeSaleProduct timeSaleProduct);
}
