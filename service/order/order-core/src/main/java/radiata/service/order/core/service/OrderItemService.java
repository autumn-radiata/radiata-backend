package radiata.service.order.core.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import radiata.common.annotation.Implementation;
import radiata.common.domain.order.dto.request.OrderCreateRequestDto;
import radiata.common.domain.order.dto.request.OrderItemCreateRequestDto;
import radiata.common.domain.order.dto.response.OrderItemResponseDto;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.model.entity.OrderItem;
import radiata.service.order.core.implemetation.OrderIdCreator;
import radiata.service.order.core.service.context.OrderRollbackContext;
import radiata.service.order.core.service.mapper.OrderItemMapper;

@Implementation
@Slf4j(topic = "OrderItemService")
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemMapper orderItemMapper;
    private final OrderIdCreator orderIdCreator;
    private final ProcessService processService;

    public Set<OrderItemResponseDto> toDtoSet(Set<OrderItem> orderItems) {

        return orderItems.stream()
            .map(orderItemMapper::toDto)
            .collect(Collectors.toSet());
    }

    // 주문 상품 처리 로직
    public void processOrderItems(OrderCreateRequestDto requestDto, Order order, String userId) {
        // 보상 트랜잭션 관리 객체
        OrderRollbackContext rollbackContext = new OrderRollbackContext();

        Set<OrderItem> orderItems = new HashSet<>();        // 주문 상품 목록 - set
        int orderPrice = 0;                                 // 총 주문 금액 - set

        for (OrderItemCreateRequestDto itemCreateDto : requestDto.itemList()) {
            // 주문 상품 ID 생성 및 주문 상품 목록에 추가
            OrderItem orderItem = createIdAndAddOrderItem(orderItems, itemCreateDto, order);
            // 1️⃣ 타임세일 상품 재고 확인 및 차감
            processService.checkAndDeductTimeSaleProduct(rollbackContext, orderItem);
            // 2️⃣ 상품 재고 확인 및 차감
            processService.checkAndDeductStock(rollbackContext, orderItem);
            // 3️⃣ 쿠폰 사용 여부 체크
            processService.checkAndUseCoupon(rollbackContext, userId, orderItem);
            // 총 금액에 추가
            orderPrice += orderItem.getPaymentPrice();
        }

        // 4️⃣ 적립금 확인 및 차감
        int point = requestDto.point();
        processService.checkAndUsePoint(rollbackContext, order, point, userId);
        // 주문 - 금액 & 상품 목록 지정
        order.setOrderPriceAndItems(orderPrice, orderItems);
    }

    // 주문 상품 ID 생성 및 주문 상품 목록에 추가
    private OrderItem createIdAndAddOrderItem(Set<OrderItem> orderItems, OrderItemCreateRequestDto itemCreateDto,
        Order order) {

        String orderItemId = orderIdCreator.create();
        OrderItem orderItem = orderItemMapper.toEntity(itemCreateDto, orderItemId, order);
        orderItems.add(orderItem);

        return orderItem;
    }
}