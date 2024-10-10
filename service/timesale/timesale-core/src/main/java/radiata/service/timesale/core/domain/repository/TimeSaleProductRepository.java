package radiata.service.timesale.core.domain.repository;

import java.util.Optional;
import radiata.service.timesale.core.domain.TimeSaleProduct;

public interface TimeSaleProductRepository {

    TimeSaleProduct save(TimeSaleProduct timeSaleProduct);

    Optional<TimeSaleProduct> findById (String id);
}
