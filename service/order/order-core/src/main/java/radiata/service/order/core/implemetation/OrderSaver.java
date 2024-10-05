package radiata.service.order.core.implemetation;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.annotation.Implementation;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.repository.OrderRepository;

@Implementation
@RequiredArgsConstructor
public class OrderSaver {

    private final OrderRepository orderRepository;

    @Transactional
    public Order save(Order order) {

        return orderRepository.save(order);
    }
}
