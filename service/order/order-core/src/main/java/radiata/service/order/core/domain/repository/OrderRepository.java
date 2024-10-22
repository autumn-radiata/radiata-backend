package radiata.service.order.core.domain.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import radiata.service.order.core.domain.model.entity.Order;

@Repository
public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(String id);

    void delete(Order order);
}
