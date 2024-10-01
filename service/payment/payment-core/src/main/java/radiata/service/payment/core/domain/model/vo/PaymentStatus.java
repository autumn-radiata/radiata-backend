package radiata.service.payment.core.domain.model.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

    PENDING("결제 대기"),
    APPROVED("결제 승인"),
    FAILED("결제 실패"),
    SETTLED("정산 완료");

    private final String title;
}
