package radiata.common.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum SuccessMessage {

    /* 공통 */
    OK(HttpStatus.OK, "0", "정상 처리 되었습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
