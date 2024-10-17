package radiata.service.payment.core.implementation;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URI;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.payment.core.domain.model.entity.PayUser;
import radiata.service.payment.core.domain.model.entity.Payment;
import radiata.service.payment.core.service.request.TossPaymentConfirmRequestDto;

@Slf4j
@Implementation
@RequiredArgsConstructor
public class PaymentRequester {

    private final RestTemplate restTemplate;

    @Value("${toss-payment.test-secret-key}")
    private String TOSS_PAYMENT_TEST_SECRET_KEY;

    @Value("${toss-payment.confirm-url}")
    private String TOSS_PAYMENT_CONFIRM_API_URL;

    @Transactional
    public void requestTossPayment(Payment payment, String orderId) {

        RequestEntity<TossPaymentConfirmRequestDto> requestEntity = createRequestEntity(orderId, payment);
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        if (isPaymentSuccess(response)) {
            payment.approve();
        } else {
            payment.fail();
        }
    }

    private boolean isPaymentSuccess(ResponseEntity<String> response) {
        return response.getStatusCode().is2xxSuccessful();
    }

    private RequestEntity<TossPaymentConfirmRequestDto> createRequestEntity(String orderId, Payment payment) {

        TossPaymentConfirmRequestDto body = new TossPaymentConfirmRequestDto(
            orderId,
            payment.getTransactionId(),
            payment.getAmount().getAmount());

        return RequestEntity
            .post(URI.create(TOSS_PAYMENT_CONFIRM_API_URL))
            .headers(createJsonTypeHeaders())
            .body(body);
    }

    private HttpHeaders createJsonTypeHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setBasicAuth(createBasicAuth());

        return httpHeaders;
    }

    private String createBasicAuth() {
        String secretKeyWithPassword = TOSS_PAYMENT_TEST_SECRET_KEY + ":";
        return new String(Base64.getEncoder().encode(secretKeyWithPassword.getBytes(UTF_8)));
    }

    @Transactional
    public void requestEasyPay(Payment payment, PayUser payUser) {
        // 충전금 충분한지 체크
        if (payUser.isBalanceLessThan(payment.getAmount())) {
            payment.fail();
            throw new BusinessException(ExceptionMessage.INSUFFICIENT_BALANCE);
        }

        // 충전금 차감
        payUser.withdraw(payment.getAmount());
        // 결제 승인
        payment.approve();
    }
}
