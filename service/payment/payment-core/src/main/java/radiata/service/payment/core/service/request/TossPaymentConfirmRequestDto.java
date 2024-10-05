package radiata.service.payment.core.service.request;

public record TossPaymentConfirmRequestDto(
    String orderId,
    String paymentKey,
    Long amount
) {

}
