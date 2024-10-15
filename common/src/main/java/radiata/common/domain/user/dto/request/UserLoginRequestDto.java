package radiata.common.domain.user.dto.request;

import jakarta.validation.constraints.Email;

public record UserLoginRequestDto(

    @Email(message = "잘못된 이메일 주소 입니다.")
    String email,
    String password
) {

}
