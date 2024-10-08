package radiata.common.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum SuccessMessage {

    /* 주문 */
    CREATE_ORDER(HttpStatus.OK, "0", "주문 생성 완료"),
    GET_ORDERS(HttpStatus.OK, "0", "주문 목록 조회 완료"),
    GET_ORDER(HttpStatus.OK, "0", "주문 내역 조회 완료"),
    COMPLETE_ORDER_PAYMENT(HttpStatus.OK, "0", "주문 결제 완료"),
    UPDATE_ORDER(HttpStatus.OK, "0", "주문 상태 수정 완료"),
    DELETE_ORDER(HttpStatus.OK, "0", "주문 내역 삭제 완료"),
    CANCEL_ORDER(HttpStatus.OK, "0", "주문 취소 완료"),
    REFUND_ORDER(HttpStatus.OK, "0", "주문 환불 완료"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
