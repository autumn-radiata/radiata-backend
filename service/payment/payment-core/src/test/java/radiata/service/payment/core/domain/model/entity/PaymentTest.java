package radiata.service.payment.core.domain.model.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksuid.Ksuid;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import radiata.common.domain.payment.constant.PaymentStatus;
import radiata.common.domain.payment.constant.PaymentType;
import radiata.service.payment.core.domain.model.vo.Money;

@DisplayName("Payment 엔티티 테스트")
class PaymentTest {

    Payment tossPayment;

    @BeforeEach
    void setUp() {
        tossPayment = Payment.of(
            Ksuid.newKsuid().toString(),
            "user01",
            "transaction01",
            Money.of(1000),
            PaymentType.TOSS_PAYMENTS);
    }

    @Test
    @DisplayName("토스페이먼츠 결제 생성")
    void toss_payment_create() {
        // then
        assertThat(tossPayment.getId()).isNotBlank();
        assertThat(tossPayment.getUserId()).isEqualTo("user01");
        assertThat(tossPayment.getTransactionId()).isEqualTo("transaction01");
        assertThat(tossPayment.getAmount()).isEqualTo(Money.of(1000));
        assertThat(tossPayment.getStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(tossPayment.getType()).isEqualTo(PaymentType.TOSS_PAYMENTS);
    }

    @Test
    @DisplayName("간편결제 생성")
    void pay_payment_create() {
        // given
        Payment payPayment = Payment.of(
            Ksuid.newKsuid().toString(),
            "user01",
            "transaction01",
            Money.of(1000),
            PaymentType.EASY_PAY);

        // then
        assertThat(payPayment.getId()).isNotBlank();
        assertThat(payPayment.getUserId()).isEqualTo("user01");
        assertThat(payPayment.getTransactionId()).isEqualTo("transaction01");
        assertThat(payPayment.getAmount()).isEqualTo(Money.of(1000));
        assertThat(payPayment.getStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(payPayment.getType()).isEqualTo(PaymentType.EASY_PAY);
    }

    @Test
    @DisplayName("결제 성공")
    void payment_approve() {
        // given 
        TemporalUnitWithinOffset acceptableTimeOffset = new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS);

        // when
        tossPayment.approve();

        // then
        assertThat(tossPayment.getStatus()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(tossPayment.getApprovedAt()).isCloseTo(LocalDateTime.now(), acceptableTimeOffset);
    }

    @Test
    @DisplayName("결제 실패")
    void payment_fail() {
        // when
        tossPayment.fail();

        // then
        assertThat(tossPayment.getStatus()).isEqualTo(PaymentStatus.FAILED);
    }

    @Test
    @DisplayName("정산 성공")
    void payment_settle() {
        // when
        tossPayment.settle();

        // then
        assertThat(tossPayment.getStatus()).isEqualTo(PaymentStatus.SETTLED);
    }
}