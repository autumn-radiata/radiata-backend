package radiata.common.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum SuccessMessage {

    /* 공통 */
    OK(HttpStatus.OK, "0", "정상 처리 되었습니다."),

      /* 주문 */
    CREATE_ORDER(HttpStatus.OK, "0", "주문 생성 완료"),
    GET_ORDERS(HttpStatus.OK, "0", "주문 목록 조회 완료"),
    GET_ORDER(HttpStatus.OK, "0", "주문 내역 조회 완료"),
    COMPLETE_ORDER_PAYMENT(HttpStatus.OK, "0", "주문 결제 완료"),
    UPDATE_ORDER(HttpStatus.OK, "0", "주문 상태 수정 완료"),
    DELETE_ORDER(HttpStatus.OK, "0", "주문 내역 삭제 완료"),
    CANCEL_ORDER(HttpStatus.OK, "0", "주문 취소 완료"),
    REFUND_ORDER(HttpStatus.OK, "0", "주문 환불 완료");


    /* 쿠폰 */
    CREATE_COUPON(HttpStatus.OK, "0", "쿠폰이 생성 되었습니다."),
    GET_COUPONS(HttpStatus.OK, "0", "쿠폰 목록을 조회가 완료 되었습니다."),
    GET_COUPON(HttpStatus.OK, "0", "쿠폰 조회가 완료 되었습니다."),
    UPDATE_COUPON(HttpStatus.OK, "0", "쿠폰 수정이 완료 되었습니다."),
    DELETE_COUPON(HttpStatus.OK, "0", "쿠폰 삭제가 완료 되었습니다."),


    ISSUE_COUPON(HttpStatus.OK, "0", "쿠폰이 발급 되었습니다."),
    USE_COUPON_ISSUE(HttpStatus.OK, "0", "쿠폰 사용이 완료 되었습니다."),
    GET_COUPON_ISSUE(HttpStatus.OK, "0", "발급 쿠폰 조회가 완료 되었습니다."),
    GET_COUPON_ISSUES(HttpStatus.OK, "0", "발급 쿠폰 목록 조회가 완료 되었습니다."),
    DELETE_COUPON_ISSUES(HttpStatus.OK, "0", "발급 쿠폰 삭제가 완료 되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
