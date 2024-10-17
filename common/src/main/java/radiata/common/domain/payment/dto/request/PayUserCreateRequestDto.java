package radiata.common.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PayUserCreateRequestDto(

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^[0-9]{6}$", message = "비밀번호는 숫자 6자리로 입력해주세요.")
    String password
) {

}
