package radiata.service.order.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.order.dto.request.OrderCreateRequestDto;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.implemetation.OrderIdCreator;
import radiata.service.order.core.implemetation.OrderSaver;
import radiata.service.order.core.service.mapper.OrderMapper;

@Service
@RequiredArgsConstructor
public class OrderCreateService {

    private final OrderIdCreator orderIdCreator;
    private final OrderSaver orderSaver;
    private final OrderMapper orderMapper;
    private final OrderItemService orderItemService;

    // 주문 생성
    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto requestDto, String userId) {
        // 주문 ID 생성
        String orderId = orderIdCreator.create();
        // 초기 주문 생성
        Order order = orderSaver.save(orderMapper.toEntity(requestDto, orderId, userId));
        // 주문 상품 목록 처리
        orderItemService.processOrderItems(requestDto, order, userId);
        // 주문 상품 목록 추가 & 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }
}
