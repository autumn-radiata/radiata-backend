package radiata.service.timesale.core.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.timesale.dto.condition.TimeSaleSearchCondition;
import radiata.service.timesale.core.domain.TimeSale;
import radiata.service.timesale.core.domain.TimeSaleProduct;

public interface TimeSaleQueryRepository {

    Page<TimeSale> findTimeSalesByCondition(TimeSaleSearchCondition condition, Pageable pageable);

    Optional<TimeSale> findByProductId(String productId);

    List<TimeSaleProduct> findByProductIds(List<String> productIds);
}
