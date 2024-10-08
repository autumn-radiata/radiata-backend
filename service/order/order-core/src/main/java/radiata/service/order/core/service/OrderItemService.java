package radiata.service.order.core.service;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import radiata.common.annotation.Implementation;
import radiata.common.domain.order.dto.response.OrderItemResponseDto;
import radiata.service.order.core.domain.model.entity.OrderItem;
import radiata.service.order.core.service.mapper.OrderItemMapper;

@Implementation
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemMapper orderItemMapper;

    public Set<OrderItemResponseDto> toDtoSet(Set<OrderItem> orderItems) {

        return orderItems.stream()
            .map(orderItemMapper::toDto)
            .collect(Collectors.toSet());
    }
}
