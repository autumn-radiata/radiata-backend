package radiata.service.order.core.implemetation;

import lombok.RequiredArgsConstructor;
import radiata.common.annotation.Implementation;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.repository.OrderRepository;

@Implementation
@RequiredArgsConstructor
public class OrderRemover {

    private final OrderRepository orderRepository;

    // 영구 삭제
    public void delete(Order order) {
        orderRepository.delete(order);
    }
}
