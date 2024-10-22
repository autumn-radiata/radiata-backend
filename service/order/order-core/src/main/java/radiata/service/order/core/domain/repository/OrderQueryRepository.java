package radiata.service.order.core.domain.repository;

import java.util.List;
import radiata.service.order.core.domain.model.entity.Order;

public interface OrderQueryRepository {

    List<Order> findOrdersNotCompletedWithin10Minutes();
}