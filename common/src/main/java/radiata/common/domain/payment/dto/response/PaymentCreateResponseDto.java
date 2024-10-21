package radiata.common.domain.payment.dto.response;

public record PaymentCreateResponseDto(
    Boolean isPaymentSuccess,
    String paymentId
) {

}
