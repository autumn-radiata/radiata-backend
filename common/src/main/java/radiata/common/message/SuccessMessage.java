package radiata.common.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum SuccessMessage {




    /* 쿠폰 */
    CREATE_COUPON(HttpStatus.OK, "0", "쿠폰이 생성 되었습니다."),
    GET_COUPONS(HttpStatus.OK, "0", "쿠폰 목록을 조회가 완료 되었습니다."),
    GET_COUPON(HttpStatus.OK, "0", "쿠폰 조회가 완료 되었습니다."),
    UPDATE_COUPON(HttpStatus.OK, "0", "쿠폰 수정이 완료 되었습니다."),
    DELETE_COUPON(HttpStatus.OK, "0", "쿠폰 삭제가 완료 되었습니다."),


    ISSUE_COUPON(HttpStatus.OK, "0", "쿠폰이 발급 되었습니다."),
    USE_COUPON_ISSUE(HttpStatus.OK, "0", "쿠폰 사용이 완료 되었습니다."),
    GET_COUPON_ISSUE(HttpStatus.OK, "0", "발급 쿠폰 조회가 완료 되었습니다."),
    GET_COUPON_ISSUES(HttpStatus.OK, "0", "발급 쿠폰 목록 조회가 완료 되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
