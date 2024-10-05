package radiata.service.payment.core.domain.repository;

import org.springframework.stereotype.Repository;
import radiata.service.payment.core.domain.model.entity.Payment;

@Repository
public interface PaymentRepository {

    Payment save(Payment payment);
}
