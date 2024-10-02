package radiata.service.order.core.domain.service;

import com.github.ksuid.KsuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.order.dto.request.OrderCreateRequestDto;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl {

    private final OrderRepository orderRepository;


    // 주문 ID 생성
    private String createOrderId() {
        return KsuidGenerator.generate();
    }


    // 주문 생성
    @Transactional
    public Order createOrder(OrderCreateRequestDto createRequestDto, String orderId, String userId) {

        Order order = Order.of(
            orderId,
            userId,
            createRequestDto.address(),
            createRequestDto.comment()
        );

        orderRepository.save(order);

        return order;
    }


    // 주문 상세 조회
    @Transactional(readOnly = true)
    public Order getOrder(String orderId) {

        return orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }


    // TODO 결제(대기중, 완료), 배송(중, 대기중, 완료) - 사용 예정
    // 주문 상태 변경
    @Transactional
    public void updateOrderStatus(String orderId, OrderStatus updateStatus) {

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.updateOrderStatus(updateStatus);
    }

}
