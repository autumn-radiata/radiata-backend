package radiata.service.timesale.core.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import radiata.service.timesale.core.domain.TimeSale;
import radiata.service.timesale.core.domain.repository.TimeSaleRepository;

public interface TimeSaleJpaRepository extends JpaRepository<TimeSale, String>, TimeSaleRepository {

}
