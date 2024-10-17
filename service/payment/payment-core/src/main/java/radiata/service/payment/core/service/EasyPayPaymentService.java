package radiata.service.payment.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.payment.constant.PaymentStatus;
import radiata.common.domain.payment.dto.request.EasyPayCreateRequestDto;
import radiata.common.domain.payment.dto.response.PaymentCreateResponseDto;
import radiata.service.payment.core.domain.model.entity.PayUser;
import radiata.service.payment.core.domain.model.entity.Payment;
import radiata.service.payment.core.implementation.PayUserReader;
import radiata.service.payment.core.implementation.PayUserValidator;
import radiata.service.payment.core.implementation.PaymentRequester;
import radiata.service.payment.core.implementation.PaymentSaver;

@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayPaymentService {

    private final PaymentSaver paymentSaver;
    private final PayUserReader payUserReader;
    private final PayUserValidator payUserValidator;
    private final PaymentRequester paymentRequester;

    /**
     * 간편결제 요청
     */
    @Transactional
    public PaymentCreateResponseDto requestTossPayment(EasyPayCreateRequestDto request) {
        // 간편결제 유저 조회
        PayUser payUser = payUserReader.getPayUserByUserId(request.userId());
        // 간편결제 유저 비밀번호 검증
        payUserValidator.validatePassword(payUser.getPassword(), request.password());
        // 결제 생성
        Payment payment = paymentSaver.createEasyPay(request.userId(), request.amount());
        // 결제 요청
        paymentRequester.requestEasyPay(payment, payUser);

        return new PaymentCreateResponseDto(payment.getStatus().equals(PaymentStatus.APPROVED), payment.getId());
    }
}
