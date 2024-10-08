package radiata.service.payment.core.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.github.ksuid.KsuidGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import radiata.service.payment.core.domain.model.entity.Payment;
import radiata.service.payment.core.domain.model.vo.Money;
import radiata.service.payment.core.domain.model.vo.PaymentType;
import radiata.service.payment.core.domain.repository.PaymentRepository;

@DisplayName("PaymentSaver 테스트")
@ExtendWith(MockitoExtension.class)
class PaymentSaverTest {

    @InjectMocks
    private PaymentSaver paymentSaver;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("토스 결제 생성 테스트")
    void createTossPayment() {
        // given
        String userId = KsuidGenerator.generate();
        String paymentKey = "paymentKey-01"; // 토스페이먼츠 고유 결제 키
        long amount = 1000L;
        given(paymentRepository.save(any(Payment.class))).willReturn(Payment.of(
            KsuidGenerator.generate(),
            userId,
            paymentKey,
            Money.of(amount),
            PaymentType.TOSS_PAYMENTS
        ));

        // when
        Payment savedPayment = paymentSaver.createTossPayment(userId, paymentKey, amount);

        // then
        assertThat(savedPayment).isNotNull();
        assertThat(savedPayment.getUserId()).isEqualTo(userId);
        assertThat(savedPayment.getTransactionId()).isEqualTo(paymentKey);
        assertThat(savedPayment.getAmount().getAmount()).isEqualTo(amount);
        assertThat(savedPayment.getType()).isEqualTo(PaymentType.TOSS_PAYMENTS);
    }
}