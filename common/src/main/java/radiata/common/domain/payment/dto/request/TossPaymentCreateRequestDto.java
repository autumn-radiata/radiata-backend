package radiata.common.domain.payment.dto.request;

public record TossPaymentCreateRequestDto(
    String userId,
    String orderId,
    String paymentKey,
    Long amount
) {

}
