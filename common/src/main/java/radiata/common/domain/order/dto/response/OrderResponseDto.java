package radiata.common.domain.order.dto.response;

import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record OrderResponseDto(String orderId, String userId, String status, Integer orderPrice, Boolean isRefunded,
                               String address, String paymentId, String comment, Set<OrderItemResponseDto> orderItems) {

    public static OrderResponseDto of(
        String orderId,
        String userId,
        String status,
        Integer orderPrice,
        Boolean isRefunded,
        String address,
        String paymentId,
        String comment
    ) {

        return OrderResponseDto.builder()
            .orderId(orderId)
            .userId(userId)
            .status(status)
            .orderPrice(orderPrice)
            .isRefunded(isRefunded)
            .address(address)
            .paymentId(paymentId)
            .comment(comment)
            .build();
    }

    public OrderResponseDto withItemList(Set<OrderItemResponseDto> orderItems) {
        return OrderResponseDto.builder()
            .orderId(orderId)
            .userId(userId)
            .status(status)
            .orderPrice(orderPrice)
            .isRefunded(isRefunded)
            .address(address)
            .paymentId(paymentId)
            .comment(comment)
            .orderItems(orderItems)
            .build();
    }
}
