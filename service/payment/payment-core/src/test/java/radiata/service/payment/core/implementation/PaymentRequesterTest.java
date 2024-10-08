package radiata.service.payment.core.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;

import com.github.ksuid.KsuidGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import radiata.service.payment.core.domain.model.entity.Payment;
import radiata.service.payment.core.domain.model.vo.Money;
import radiata.service.payment.core.domain.model.vo.PaymentStatus;
import radiata.service.payment.core.domain.model.vo.PaymentType;

@DisplayName("PaymentRequester 테스트")
@ExtendWith(MockitoExtension.class)
class PaymentRequesterTest {

    @InjectMocks
    private PaymentRequester paymentRequester;

    @Mock
    private RestTemplate restTemplate;

    @Test
    @DisplayName("토스 결제 요청 성공 테스트")
    void request_toss_payment_success() {
        // given
        String orderId = KsuidGenerator.generate();
        Payment payment = Payment.of(
            KsuidGenerator.generate(),
            KsuidGenerator.generate(),
            "paymentKey-01",
            Money.of(1000L),
            PaymentType.TOSS_PAYMENTS
        );

        ReflectionTestUtils.setField(
            paymentRequester,
            "TOSS_PAYMENT_CONFIRM_API_URL",
            "https://api.tosspayments.com/v1/payments/confirm");

        ReflectionTestUtils.setField(
            paymentRequester,
            "TOSS_PAYMENT_TEST_SECRET_KEY",
            " test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6");

        given(restTemplate.exchange(any(RequestEntity.class), eq(String.class)))
            .willReturn(ResponseEntity.ok().build());

        // when
        paymentRequester.requestTossPayment(payment, orderId);

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.APPROVED);
    }

    @Test
    @DisplayName("토스 결제 요청 실패 테스트")
    void request_toss_payment_fail() {
        // given
        String orderId = KsuidGenerator.generate();
        Payment payment = Payment.of(
            KsuidGenerator.generate(),
            KsuidGenerator.generate(),
            "paymentKey-01",
            Money.of(1000L),
            PaymentType.TOSS_PAYMENTS
        );

        ReflectionTestUtils.setField(
            paymentRequester,
            "TOSS_PAYMENT_CONFIRM_API_URL",
            "https://api.tosspayments.com/v1/payments/confirm");

        ReflectionTestUtils.setField(
            paymentRequester,
            "TOSS_PAYMENT_TEST_SECRET_KEY",
            " test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6");

        given(restTemplate.exchange(any(RequestEntity.class), eq(String.class)))
            .willReturn(ResponseEntity.badRequest().build());

        // when
        paymentRequester.requestTossPayment(payment, orderId);

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
    }
}