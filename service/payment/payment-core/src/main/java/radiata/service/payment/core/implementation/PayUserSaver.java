package radiata.service.payment.core.implementation;

import com.github.ksuid.KsuidGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.payment.core.domain.model.entity.PayUser;
import radiata.service.payment.core.domain.repository.PayUserRepository;

@Slf4j
@Implementation
@RequiredArgsConstructor
public class PayUserSaver {

    private final PasswordEncoder passwordEncoder;
    private final PayUserRepository payUserRepository;

    @Transactional
    public PayUser createPayUser(String userId, String password) {
        if (payUserRepository.existsByUserId(userId)) {
            throw new BusinessException(ExceptionMessage.PAY_USER_DUPLICATE);
        }

        PayUser payUser = PayUser.of(
            KsuidGenerator.generate(),
            userId,
            passwordEncoder.encode(password)
        );

        return payUserRepository.save(payUser);
    }
}
