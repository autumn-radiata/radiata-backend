package radiata.common.domain.payment.dto.response;

public record TossPaymentCreateResponseDto(
    Boolean isPaymentSuccess,
    String paymentId
) {

}
