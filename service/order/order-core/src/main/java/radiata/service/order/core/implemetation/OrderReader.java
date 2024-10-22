package radiata.service.order.core.implemetation;

import static radiata.common.message.ExceptionMessage.NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.repository.OrderQueryRepository;
import radiata.service.order.core.domain.repository.OrderRepository;

@Implementation
@RequiredArgsConstructor
public class OrderReader {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @Transactional(readOnly = true)
    public Order readOrder(String orderId) {

        return orderRepository.findById(orderId)
            .orElseThrow(() -> new BusinessException(NOT_FOUND));
    }


    @Transactional(readOnly = true)
    public List<Order> readOrders() {

        return orderQueryRepository.findOrdersNotCompletedWithin10Minutes();
    }
}