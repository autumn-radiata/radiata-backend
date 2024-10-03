package radiata.common.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

public record UserCreateRequestDto(

    @NotBlank(message = "사용자 이름은 필수 입력입니다.")
    String username, //사용자 아이디

    @Pattern(regexp = "^[A-Za-z\\d!@#$%&*()]{8,15}$",
        message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자를 사용 가능하며, 최소 8자 이상 15자 이하 여야합니다.")
    String password, // 비밀번호

    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "잘못된 이메일 주소 입니다.")
    String email, //이메일

    String nickname, //닉네임

    @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
        message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야합니다.")
    String phone, //핸드폰 번호

    String roadAddress, // 도로명 주소

    String detailAddress, // 상세주소

    String zipcode // 우편번호
){

}
