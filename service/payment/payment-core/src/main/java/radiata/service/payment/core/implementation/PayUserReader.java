package radiata.service.payment.core.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.payment.core.domain.model.entity.PayUser;
import radiata.service.payment.core.domain.repository.PayUserRepository;

@Slf4j
@Implementation
@RequiredArgsConstructor
public class PayUserReader {

    private final PayUserRepository payUserRepository;

    @Transactional(readOnly = true)
    public PayUser getPayUserById(String payUserId) {
        return payUserRepository.findById(payUserId)
            .orElseThrow(() -> new BusinessException(ExceptionMessage.PAY_USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public PayUser getPayUserByUserId(String userId) {
        return payUserRepository.findByUserId(userId)
            .orElseThrow(() -> new BusinessException(ExceptionMessage.PAY_USER_NOT_FOUND));
    }
}
