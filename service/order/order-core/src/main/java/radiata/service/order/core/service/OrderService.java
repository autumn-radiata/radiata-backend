package radiata.service.order.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.order.dto.request.OrderCreateRequestDto;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.implemetation.OrderIdCreator;
import radiata.service.order.core.implemetation.OrderReader;
import radiata.service.order.core.implemetation.OrderSaver;
import radiata.service.order.core.implemetation.OrderUpdater;
import radiata.service.order.core.service.mapper.OrderMapper;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderIdCreator orderIdCreator;
    private final OrderSaver orderSaver;
    private final OrderUpdater orderUpdater;
    private final OrderMapper orderMapper;
    private final OrderReader orderReader;

    // 주문 생성
    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto requestDto, String userId) {
        // 주문 ID 생성
        String orderId = orderIdCreator.createOrderId();
        // 주문 생성
        Order order = orderSaver.save(orderMapper.toEntity(requestDto, orderId, userId));
        // 반환
        return orderMapper.toDto(order);
    }

    // 주문 상태 변경
    @Transactional
    public OrderResponseDto updateOrderStatus(OrderStatus requestStatus, String orderId) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 주문 상태 변경
        orderUpdater.updateOrderStatus(requestStatus, order);
        // 반환
        return orderMapper.toDto(order);
    }
}
