package radiata.service.order.core.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.implemetation.OrderReader;
import radiata.service.order.core.implemetation.OrderRemover;
import radiata.service.order.core.implemetation.OrderValidator;
import radiata.service.order.core.service.context.OrderRollbackContext;
import radiata.service.order.core.service.mapper.OrderMapper;

@Service
@RequiredArgsConstructor
public class OrderReadService {

    private final OrderMapper orderMapper;
    private final OrderReader orderReader;
    private final OrderRemover orderRemover;
    private final OrderValidator orderValidator;
    private final OrderItemService orderItemService;
    private final RollbackService rollbackService;

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

    // 결제 완료가 되지않은 생성된지 10분이 지난 주문 자동삭제
    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void cancelUnpaidOrder() {
        List<Order> orders = orderReader.readOrders();

        orders.forEach(order -> {
            // 롤백
            OrderRollbackContext rollbackContext = rollbackService.collectOrderItemInfo(order.getOrderItems());
            rollbackService.createOrderItemsRollback(rollbackContext);
            // 삭제
            orderRemover.delete(order);
        });
    }
}
