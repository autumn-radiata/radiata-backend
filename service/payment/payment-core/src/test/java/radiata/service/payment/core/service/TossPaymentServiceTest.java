package radiata.service.payment.core.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

import com.github.ksuid.KsuidGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import radiata.common.domain.payment.dto.request.TossPaymentCreateRequestDto;
import radiata.common.domain.payment.dto.response.PaymentCreateResponseDto;
import radiata.service.payment.core.domain.model.entity.Payment;
import radiata.service.payment.core.implementation.PaymentRequester;
import radiata.service.payment.core.implementation.PaymentSaver;

@DisplayName("TossPaymentService 테스트")
@ExtendWith(MockitoExtension.class)
class TossPaymentServiceTest {

    @InjectMocks
    private TossPaymentService tossPaymentService;

    @Mock
    private PaymentSaver paymentSaver;

    @Mock
    private PaymentRequester paymentRequester;

    @Test
    @DisplayName("토스 결제 요청 성공 테스트")
    void request_toss_payment_success() {
        // given
        String userId = KsuidGenerator.generate();
        String orderId = KsuidGenerator.generate();
        String paymentId = KsuidGenerator.generate();
        String paymentKey = "tosspaymentkey-01";
        long amount = 1000L;

        Payment payment = spy(Payment.class);
        TossPaymentCreateRequestDto requestDto = new TossPaymentCreateRequestDto(userId, orderId, paymentKey, amount);

        given(paymentSaver.createTossPayment(anyString(), eq(paymentKey), anyLong())).will(
            invocation -> {
                ReflectionTestUtils.setField(payment, "id", paymentId);
                ReflectionTestUtils.setField(payment, "transactionId", paymentKey);
                return payment;
            });

        doAnswer(invocation -> {
            Payment paymentArgument = invocation.getArgument(0);
            paymentArgument.approve();
            ReflectionTestUtils.setField(paymentArgument, "id", paymentId);
            return null;
        }).when(paymentRequester).requestTossPayment(eq(payment), anyString());

        // when
        PaymentCreateResponseDto responseDto = tossPaymentService.requestTossPayment(requestDto);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.isPaymentSuccess()).isTrue();
        assertThat(responseDto.paymentId()).isEqualTo(paymentId);
    }

    @Test
    @DisplayName("토스 결제 요청 실패 테스트")
    void request_toss_payment_fail() {
        // given
        String userId = KsuidGenerator.generate();
        String orderId = KsuidGenerator.generate();
        String paymentId = KsuidGenerator.generate();
        String paymentKey = "tosspaymentkey-01";
        long amount = 1000L;

        Payment payment = spy(Payment.class);
        TossPaymentCreateRequestDto requestDto = new TossPaymentCreateRequestDto(userId, orderId, paymentKey, amount);

        given(paymentSaver.createTossPayment(anyString(), eq(paymentKey), anyLong())).will(
            invocation -> {
                ReflectionTestUtils.setField(payment, "id", paymentId);
                ReflectionTestUtils.setField(payment, "transactionId", paymentKey);
                return payment;
            });

        doAnswer(invocation -> {
            Payment paymentArgument = invocation.getArgument(0);
            paymentArgument.fail();
            ReflectionTestUtils.setField(paymentArgument, "id", paymentId);
            return null;
        }).when(paymentRequester).requestTossPayment(eq(payment), anyString());

        // when
        PaymentCreateResponseDto responseDto = tossPaymentService.requestTossPayment(requestDto);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.isPaymentSuccess()).isFalse();
        assertThat(responseDto.paymentId()).isEqualTo(paymentId);
    }
}