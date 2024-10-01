package radiata.common.message;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {

    /* 글로벌 1000번대 */

    // 권한 없음 403
    NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "1000", "해당 요청에 대한 권한이 없습니다."),
    // 잘못된 형식의 입력 400
    INVALID_INPUT(BAD_REQUEST, "1001", "유효하지 않은 입력값입니다."),
    // 존재하지 않는 값 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "1002", "존재하지 않는 입력값입니다."),
    // 시스템 에러 500
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "1003", "알 수 없는 에러가 발생했습니다."),


    /* 유저 2000번대 */
    POINT_ISSUE_LACK(BAD_REQUEST, "2001", "적립금이 부족합니다."),



    ;




    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
