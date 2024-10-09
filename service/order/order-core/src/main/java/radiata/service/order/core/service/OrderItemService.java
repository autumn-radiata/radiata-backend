package radiata.service.order.core.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import radiata.common.annotation.Implementation;
import radiata.common.domain.order.dto.response.OrderItemResponseDto;
import radiata.service.order.core.domain.model.entity.OrderItem;
import radiata.service.order.core.service.mapper.OrderItemMapper;

@Implementation
@Slf4j(topic = "OrderItemService")
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemMapper orderItemMapper;

    public Set<OrderItemResponseDto> toDtoSet(Set<OrderItem> orderItems) {

        return orderItems.stream()
            .map(orderItemMapper::toDto)
            .collect(Collectors.toSet());
    }


    // SAGA 보상 트랜잭션
    public void rollbackTransaction(List<String> deductedProducts, List<String> usedCoupons) {

        try {
            // 상품 재고 - 보상
            deductedProducts.forEach(productInfo -> {
                // TODO - kafka 를 이용한 비동기 처리
            });
            // 쿠폰 상태 값 - 보상
            usedCoupons.forEach(couponId -> {
                // TODO - kafka 를 이용한 비동기 처리
            });

        } catch (Exception e) {
            log.info("##########[보상 트랜잭션 처리 중, 예외 발생]##########");
            throw new RuntimeException(e);
        }
    }
}
