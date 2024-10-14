package radiata.common.domain.order.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record OrderItemResponseDto(String orderItemId, String orderId, String productId, String timeSaleProductId,
                                   String couponIssuedId, Integer quantity, Integer unitPrice, Integer paymentPrice) {

    public static OrderItemResponseDto of(
        String orderItemId,
        String orderId,
        String productId,
        String timeSaleProductId,
        String couponIssuedId,
        Integer quantity,
        Integer unitPrice,
        Integer paymentPrice
    ) {

        return OrderItemResponseDto.builder()
            .orderItemId(orderItemId)
            .orderId(orderId)
            .productId(productId)
            .timeSaleProductId(timeSaleProductId)
            .couponIssuedId(couponIssuedId)
            .quantity(quantity)
            .unitPrice(unitPrice)
            .paymentPrice(paymentPrice)
            .build();
    }
}
