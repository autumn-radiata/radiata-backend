package radiata.service.payment.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.payment.dto.request.TossPaymentCreateRequestDto;
import radiata.common.domain.payment.dto.response.TossPaymentCreateResponseDto;
import radiata.service.payment.core.domain.model.entity.Payment;
import radiata.service.payment.core.domain.model.vo.PaymentStatus;
import radiata.service.payment.core.implementation.PaymentRequester;
import radiata.service.payment.core.implementation.PaymentSaver;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentService {

    private final PaymentSaver paymentSaver;
    private final PaymentRequester paymentRequester;

    @Transactional
    public TossPaymentCreateResponseDto requestTossPayment(TossPaymentCreateRequestDto request) {
        // 결제 생성
        Payment payment = paymentSaver.createTossPayment(request.userId(), request.paymentKey(), request.amount());
        // 토스 결제 요청
        paymentRequester.requestTossPayment(payment, request.orderId());

        return new TossPaymentCreateResponseDto(payment.getStatus().equals(PaymentStatus.APPROVED));
    }
}
