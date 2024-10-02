package radiata.service.payment.core.implementation;

import com.github.ksuid.KsuidGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import radiata.common.annotation.Implementation;
import radiata.service.payment.core.domain.model.entity.Payment;
import radiata.service.payment.core.domain.model.vo.Money;
import radiata.service.payment.core.domain.model.vo.PaymentType;
import radiata.service.payment.core.domain.repository.PaymentRepository;

@Slf4j
@Implementation
@RequiredArgsConstructor
public class PaymentSaver {

    private final PaymentRepository paymentRepository;

    public Payment createTossPayment(String userId, String paymentKey, Long amount) {
        Payment payment = Payment.of(
            KsuidGenerator.generate(),
            userId,
            paymentKey,
            Money.of(amount),
            PaymentType.TOSS_PAYMENTS);

        return paymentRepository.save(payment);
    }
}
