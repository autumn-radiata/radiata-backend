package radiata.common.domain.order.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record OrderResponseDto(String orderId, String userId, String status, Integer orderPrice, Boolean isRefunded,
                               String address, String paymentId, String comment, List<OrderItemResponseDto> itemList) {

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
}
