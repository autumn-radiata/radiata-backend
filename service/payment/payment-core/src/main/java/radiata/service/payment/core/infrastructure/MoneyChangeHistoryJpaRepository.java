package radiata.service.payment.core.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import radiata.service.payment.core.domain.model.entity.MoneyChangeHistory;
import radiata.service.payment.core.domain.repository.MoneyChangeHistoryRepository;

public interface MoneyChangeHistoryJpaRepository
    extends JpaRepository<MoneyChangeHistory, String>, MoneyChangeHistoryRepository {

}
