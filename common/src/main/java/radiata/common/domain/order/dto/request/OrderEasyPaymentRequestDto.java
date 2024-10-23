package radiata.common.domain.order.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderEasyPaymentRequestDto(

    @PositiveOrZero(message = "amount는 0 이상이어야 합니다.")
    Integer amount,

    @Pattern(regexp = "^[0-9]{6}$", message = "password는 6자리 숫자여야 합니다.")
    String password
) {

}