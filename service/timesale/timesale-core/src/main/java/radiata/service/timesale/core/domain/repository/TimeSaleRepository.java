package radiata.service.timesale.core.domain.repository;

import java.util.Optional;
import radiata.service.timesale.core.domain.TimeSale;

public interface TimeSaleRepository {

    TimeSale save(TimeSale timeSale);

    Optional<TimeSale> findById(String timeSaleId);
}
