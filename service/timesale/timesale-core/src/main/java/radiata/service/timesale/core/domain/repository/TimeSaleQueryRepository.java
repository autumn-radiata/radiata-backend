package radiata.service.timesale.core.domain.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.timesale.dto.condition.TimeSaleSearchCondition;
import radiata.service.timesale.core.domain.TimeSale;

public interface TimeSaleQueryRepository {

    Page<TimeSale> findTimeSalesByCondition(TimeSaleSearchCondition condition, Pageable pageable);

    Optional<TimeSale> findByProductId(String productId);
}
