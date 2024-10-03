package radiata.service.payment.core.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import radiata.service.payment.core.domain.model.entity.FirmBankingRequestHistory;
import radiata.service.payment.core.domain.repository.FirmBankingRequestHistoryRepository;

public interface FirmBankingRequestHistoryJpaRepository
    extends JpaRepository<FirmBankingRequestHistory, String>, FirmBankingRequestHistoryRepository {

}
