package radiata.service.timesale.core.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import radiata.service.timesale.core.domain.TimeSaleProduct;
import radiata.service.timesale.core.domain.repository.TimeSaleProductRepository;

public interface TimeSaleProductJpaRepository extends JpaRepository<TimeSaleProduct, String>,
    TimeSaleProductRepository {

}
