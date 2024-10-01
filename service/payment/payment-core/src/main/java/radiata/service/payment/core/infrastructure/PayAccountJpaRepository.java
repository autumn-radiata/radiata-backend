package radiata.service.payment.core.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import radiata.service.payment.core.domain.model.entity.PayAccount;
import radiata.service.payment.core.domain.repository.PayAccountRepository;

public interface PayAccountJpaRepository extends JpaRepository<PayAccount, String>, PayAccountRepository {

}
