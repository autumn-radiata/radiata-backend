package radiata.service.payment.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.payment.constant.PaymentType;
import radiata.service.payment.core.domain.model.entity.PayUser;
import radiata.service.payment.core.domain.model.entity.Payment;
import radiata.service.payment.core.implementation.PayUserReader;
import radiata.service.payment.core.implementation.PaymentReader;
import radiata.service.payment.core.implementation.PaymentRequester;
import radiata.service.payment.core.implementation.PaymentSaver;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentRollbackService {

    private final PaymentSaver paymentSaver;
    private final PayUserReader payUserReader;
    private final PaymentReader paymentReader;
    private final PaymentRequester paymentRequester;

    @Transactional
    public void rollbackPayment(String paymentId) {
        // 결제 조회
        Payment payment = paymentReader.getPaymentById(paymentId);
        // 결제 취소
        if (payment.getType().equals(PaymentType.EASY_PAY)) {
            // 간편결제 유저 조회
            PayUser payUser = payUserReader.getPayUserByUserId(payment.getUserId());
            // 간편결제 취소
            paymentRequester.cancelEasyPay(payment, payUser);
        } else if (payment.getType().equals(PaymentType.TOSS_PAYMENTS)) {
            // 토스 결제 취소
            paymentRequester.cancelTossPayment(payment);
        }

        // 결제 저장
        paymentSaver.save(payment);
    }
}
