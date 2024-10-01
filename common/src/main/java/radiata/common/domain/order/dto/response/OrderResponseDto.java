package radiata.common.domain.order.dto.response;

public record OrderResponseDto(
    String orderId,
    String userId,
    String status,
    Integer orderPrice,
    Boolean isRefunded,
    String address,
    String paymentId,
    String comment
) {

}
