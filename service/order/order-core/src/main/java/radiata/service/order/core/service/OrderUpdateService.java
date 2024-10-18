package radiata.service.order.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.implemetation.OrderReader;
import radiata.service.order.core.implemetation.OrderValidator;
import radiata.service.order.core.service.mapper.OrderMapper;

@Service
@RequiredArgsConstructor
public class OrderUpdateService {

    private final OrderMapper orderMapper;
    private final OrderItemService orderItemService;
    private final OrderReader orderReader;
    private final OrderValidator orderValidator;

    // 주문 상태 변경 (배송 대기 중)
    @Transactional
    public OrderResponseDto updateStatusPendingShipping(String orderId) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 주문 상태 체크 & 변경(배송 대기 중)
        orderValidator.checkCanMoveToNextStatus(order, OrderStatus.SHIPPING_PENDING);
        // 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }

    // 주문 상태 변경 (배송 중)
    @Transactional
    public OrderResponseDto updateStatusShipping(String orderId) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 주문 상태 체크 & 변경(배송 중)
        orderValidator.checkCanMoveToNextStatus(order, OrderStatus.SHIPPING_IN_PROGRESS);
        // 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }

    // 주문 상태 변경 (배송 완료)
    @Transactional
    public OrderResponseDto updateStatusCompletedShipping(String orderId) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 주문 상태 체크 & 변경(배송 완료)
        orderValidator.checkCanMoveToNextStatus(order, OrderStatus.SHIPPING_COMPLETED);
        // 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }
}
