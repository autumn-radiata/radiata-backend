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
    GET_COUPON(HttpStatus.OK, "0", "쿠폰 조회가 완료 되었습니다.")


    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
