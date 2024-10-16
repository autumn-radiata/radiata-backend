package radiata.common.domain.payment.dto.response;

public record PayUserResponseDto(
    String userId,
    String payUserId,
    Long money
) {

}
