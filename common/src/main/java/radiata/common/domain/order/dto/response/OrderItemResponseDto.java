package radiata.common.domain.order.dto.response;

public record OrderItemResponseDto(
    String productId,
    String couponIssuedId,
    String rewardPointId,
    Integer quantity,
    Integer unitPrice,
    Integer paymentPrice
) {

}
