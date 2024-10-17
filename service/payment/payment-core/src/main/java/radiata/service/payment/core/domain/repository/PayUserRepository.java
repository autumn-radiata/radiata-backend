package radiata.service.payment.core.domain.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import radiata.service.payment.core.domain.model.entity.PayUser;

@Repository
public interface PayUserRepository {

    PayUser save(PayUser payUser);

    Optional<PayUser> findById(String id);

    Optional<PayUser> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
