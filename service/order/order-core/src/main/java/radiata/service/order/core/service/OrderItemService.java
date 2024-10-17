package radiata.service.order.core.service;

import feign.FeignException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import radiata.common.annotation.Implementation;
import radiata.common.domain.order.dto.request.OrderCreateRequestDto;
import radiata.common.domain.order.dto.request.OrderItemCreateRequestDto;
import radiata.common.domain.order.dto.response.CouponInfoDto;
import radiata.common.domain.order.dto.response.OrderItemResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.model.entity.OrderItem;
import radiata.service.order.core.implemetation.OrderIdCreator;
import radiata.service.order.core.service.mapper.OrderItemMapper;

@Implementation
@Slf4j(topic = "OrderItemService")
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemMapper orderItemMapper;
    private final OrderIdCreator orderIdCreator;
    private final ProcessService processService;
    private final RollbackService rollbackService;

    public Set<OrderItemResponseDto> toDtoSet(Set<OrderItem> orderItems) {

        return orderItems.stream()
            .map(orderItemMapper::toDto)
            .collect(Collectors.toSet());
    }

    // 주문 상품 처리 로직
    public void processOrderItems(OrderCreateRequestDto requestDto, Order order, String userId) {
        // 보상 트랜잭션 관리 변수
        List<String> deductedTimeSales = new ArrayList<>(); // 타임세일 재고 차감 목록
        List<String> deductedProducts = new ArrayList<>();  // 재고 차감 목록
        List<String> usedCoupons = new ArrayList<>();       // 사용된 쿠폰 목록

        Set<OrderItem> orderItems = new HashSet<>();        // 주문 상품 목록 - set
        int orderPrice = 0;                                 // 총 주문 금액 - set

        for (OrderItemCreateRequestDto itemCreateDto : requestDto.itemList()) {
            try {
                // 주문 상품 ID 생성 및 주문 상품 목록에 추가
                OrderItem orderItem = createIdAndAddOrderItem(orderItems, itemCreateDto, order);
                int quantity = itemCreateDto.quantity();
                // 1️⃣ 타임세일 상품 재고 확인 및 차감
                String timeSaleProductId = itemCreateDto.timeSaleProductId();
                processService.checkAndDeductTimeSaleProduct(deductedTimeSales, timeSaleProductId);
                // 2️⃣ 상품 재고 확인 및 차감
                String productId = itemCreateDto.productId();
                processService.checkAndDeductStock(deductedProducts, productId, quantity);
                // 3️⃣ 쿠폰 사용 여부 체크
                String couponIssuedId = itemCreateDto.couponIssuedId();
                CouponInfoDto couponInfoDto = processService.checkAndUseCoupon(usedCoupons, couponIssuedId, userId);
                // 상품 금액 설정 및 총 금액에 추가
                orderPrice += orderItem.setPrice(couponInfoDto.saleType(), couponInfoDto.discountValue());

            } catch (FeignException e) {
                // 보상 트랜잭션
                rollbackService.createOrderItemsRollbackTransaction(deductedTimeSales, deductedProducts, usedCoupons);
                throw new BusinessException(ExceptionMessage.ORDER_CREATION_FAILED);
            }
        }

        // 적립금 사용 시
        try {
            // TODO - process에 구현해야 함
            int point = requestDto.point();

        } catch (FeignException e) {
            // 보상 트랜잭션 - 비동기 처리 (Kafka 사용)
            rollbackService.createOrderItemsRollbackTransaction(deductedTimeSales, deductedProducts, usedCoupons);
            throw new BusinessException(ExceptionMessage.ORDER_CREATION_FAILED);
        }

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