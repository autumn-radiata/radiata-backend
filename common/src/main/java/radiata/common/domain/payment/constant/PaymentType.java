package radiata.common.domain.payment.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    TOSS_PAYMENTS("토스페이먼츠"),
    RADIATA_PAY("간편결제");

    private final String title;
}
