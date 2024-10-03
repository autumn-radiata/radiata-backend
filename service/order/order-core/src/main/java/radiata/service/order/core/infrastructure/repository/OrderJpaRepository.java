package radiata.service.order.core.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.repository.OrderRepository;

public interface OrderJpaRepository extends JpaRepository<Order, String>, OrderRepository {

}
