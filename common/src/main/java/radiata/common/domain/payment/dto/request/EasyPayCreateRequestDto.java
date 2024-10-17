package radiata.common.domain.payment.dto.request;

public record EasyPayCreateRequestDto(
    String userId,
    Long amount,
    String password
) {

}
