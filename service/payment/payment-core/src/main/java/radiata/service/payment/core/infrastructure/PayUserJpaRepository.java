package radiata.service.payment.core.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import radiata.service.payment.core.domain.model.entity.PayUser;
import radiata.service.payment.core.domain.repository.PayUserRepository;

public interface PayUserJpaRepository extends JpaRepository<PayUser, String>, PayUserRepository {

    Optional<PayUser> findByUserId(String userId);
}
