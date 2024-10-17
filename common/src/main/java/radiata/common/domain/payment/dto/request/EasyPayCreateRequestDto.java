package radiata.common.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EasyPayCreateRequestDto(

    @NotBlank(message = "userId는 필수입니다.")
    String userId,

    @NotBlank(message = "orderId는 필수입니다.")
    Long amount,

    @Pattern(regexp = "^[0-9]{6}$", message = "password는 6자리 숫자여야 합니다.")
    String password
) {

}
