package radiata.service.order.core.service.mapper;

import org.springframework.stereotype.Component;
import radiata.common.domain.order.dto.request.OrderCreateRequestDto;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.service.order.core.domain.model.entity.Order;

@Component
public class OrderMapper {

    public Order toEntity(OrderCreateRequestDto requestDto, String orderId, String userId) {

        return Order.of(
            orderId,
            userId,
            requestDto.address(),
            requestDto.comment()
        );
    }

    public OrderResponseDto toDto(Order order) {

        return OrderResponseDto.of(
            order.getId(),
            order.getUserId(),
            order.getStatus().toString(),
            order.getOrderPrice(),
            order.getIsRefunded(),
            order.getAddress(),
            order.getPaymentId(),
            order.getComment()
        );
    }
}
