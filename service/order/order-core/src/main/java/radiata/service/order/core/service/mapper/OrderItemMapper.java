package radiata.service.order.core.service.mapper;

import org.springframework.stereotype.Component;
import radiata.common.domain.order.dto.request.OrderItemCreateRequestDto;
import radiata.common.domain.order.dto.response.OrderItemResponseDto;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.model.entity.OrderItem;

@Component
public class OrderItemMapper {

    public OrderItem toEntity(OrderItemCreateRequestDto requestDto, String orderItemId, Order order) {

        return OrderItem.of(
            orderItemId,
            order,
            requestDto.productId(),
            requestDto.couponIssuedId(),
            requestDto.rewardPointId(),
            requestDto.quantity(),
            requestDto.unitPrice()
        );
    }

    public OrderItemResponseDto toDto(OrderItem orderItem) {

        return OrderItemResponseDto.of(
            orderItem.getId(),
            orderItem.getOrder().getId(),
            orderItem.getProductId(),
            orderItem.getCouponIssuedId(),
            orderItem.getRewardPointId(),
            orderItem.getQuantity(),
            orderItem.getUnitPrice(),
            orderItem.getPaymentPrice()
        );
    }
}
