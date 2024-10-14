package radiata.service.order.core.domain.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    PAYMENT_REQUESTED("결제 요청 중"),
    PAYMENT_PENDING("결제 대기 중"),
    PAYMENT_COMPLETED("결제 완료"),
    SHIPPING_PENDING("배송 대기 중"),
    SHIPPING_IN_PROGRESS("배송 중"),
    SHIPPING_COMPLETED("배송 완료"),
    PAYMENT_CANCEL_REQUESTED("결제 취소 요청"),
    PAYMENT_CANCEL_COMPLETED("결제 취소 완료"),
    REFUND_IN_PROGRESS("환불 중"),
    REFUND_COMPLETED("환불 완료");

    private final String status;

    // 다음 주문 상태로 갈 수 있는지 여부를 체크
    public boolean canMoveToNextStatus(OrderStatus nextStatus) {
        return switch (this) {
            case PAYMENT_REQUESTED -> nextStatus == PAYMENT_PENDING;
            case PAYMENT_PENDING -> nextStatus == PAYMENT_COMPLETED;
            case PAYMENT_COMPLETED -> nextStatus == SHIPPING_PENDING;
            case SHIPPING_PENDING -> nextStatus == SHIPPING_IN_PROGRESS;
            case SHIPPING_IN_PROGRESS -> nextStatus == SHIPPING_COMPLETED;
            default -> false;
        };
    }
}
