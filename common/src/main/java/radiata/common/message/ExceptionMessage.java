package radiata.common.message;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.PAYMENT_REQUIRED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {

    /* 글로벌 1000번대 */

    // 권한 없음 403
    NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "1000", "해당 요청에 대한 권한이 없습니다."),
    // 잘못된 형식의 입력 400
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "1001", "유효하지 않은 입력값입니다."),
    // 존재하지 않는 값 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "1002", "존재하지 않는 입력값입니다."),
    // 시스템 에러 500
    SYSTEM_ERROR(INTERNAL_SERVER_ERROR, "1003", "알 수 없는 에러가 발생했습니다."),

    /* 결제 2000번대 */

    // 잔액 부족 402
    INSUFFICIENT_BALANCE(PAYMENT_REQUIRED, "2001", "충전금이 부족합니다."),

    /* 유저 3000번대 */

    POINT_ISSUE_LACK(BAD_REQUEST, "3001", "적립금이 부족합니다."),


    /* 주문 5000번대 */
    INVALID_ORDER_STATUS(CONFLICT, "5001", "현재 주문 상태에서는 요청하신 상태 변경이 불가합니다."),

    /* 쿠폰 6000번대 */

    // 유효하지 않은 할인율 정책
    INVALID_INPUT_COUPON_DISCOUNT_RATE(BAD_REQUEST, "6000", "쿠폰 할인율은 1% ~ 100% 만 입력할 수 있습니다."),
    // 발급 일자가 아닌데, 발급 받으려고 할 때
    COUPON_ISSUE_PERIOD(BAD_REQUEST, "6001", "쿠폰 발급 일자가 아닙니다."),
    // 쿠폰 발급 수량 초과
    COUPON_ISSUE_QUANTITY_LIMITED(BAD_REQUEST, "6002", "쿠폰이 소진되어 더 이상 발급 받으실 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
