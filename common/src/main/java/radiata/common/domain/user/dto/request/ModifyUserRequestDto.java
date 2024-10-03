package radiata.common.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;

public record ModifyUserRequestDto(
    String nickname, //닉네임

    @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
        message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야합니다.")
    String phone, //핸드폰 번호

    String roadAddress, // 도로명 주소

    String detailAddress, // 상세주소

    String zipcode // 우편번호
) {

}
