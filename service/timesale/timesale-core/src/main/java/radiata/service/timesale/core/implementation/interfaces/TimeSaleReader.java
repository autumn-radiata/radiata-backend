package radiata.service.timesale.core.implementation.interfaces;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.timesale.dto.condition.TimeSaleSearchCondition;
import radiata.service.timesale.core.domain.TimeSale;
import radiata.service.timesale.core.domain.TimeSaleProduct;

public interface TimeSaleReader {

    TimeSale read(String timeSaleId);

    Page<TimeSale> readByCondition(TimeSaleSearchCondition condition, Pageable pageable);

    TimeSale readByProductId(String productId);

    List<TimeSaleProduct> readByProductIds(List<String> productIds);
}
