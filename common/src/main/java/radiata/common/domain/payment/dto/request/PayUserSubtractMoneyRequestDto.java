package radiata.common.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record PayUserSubtractMoneyRequestDto(

    @Positive(message = "금액은 0보다 커야 합니다.")
    @NotNull(message = "금액은 필수입니다.")
    Long amount,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^[0-9]{6}$", message = "비밀번호는 숫자 6자리로 입력해주세요.")
    String password
) {

}
