package radiata.service.payment.core.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import radiata.service.payment.core.domain.model.entity.Payment;
import radiata.service.payment.core.domain.repository.PaymentRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, String>, PaymentRepository {

}
