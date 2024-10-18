package radiata.service.order.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.implemetation.OrderReader;
import radiata.service.order.core.implemetation.OrderValidator;
import radiata.service.order.core.service.mapper.OrderMapper;

@Service
@RequiredArgsConstructor
public class OrderReadService {

    private final OrderMapper orderMapper;
    private final OrderReader orderReader;
    private final OrderValidator orderValidator;
    private final OrderItemService orderItemService;

    // 주문 상세 조회
    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(String orderId, String userId) {
        // 주문 조회
        Order order = orderReader.readOrder(orderId);
        // 사용자의 주문 내역인지 확인
        orderValidator.validateUserOwnsOrder(order.getUserId(), userId);
        // 주문 상품 목록 추가 & 반환
        return orderMapper.toDto(order).withItemList(orderItemService.toDtoSet(order.getOrderItems()));
    }
}
