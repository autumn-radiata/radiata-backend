package radiata.service.payment.core.implementation;

import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;

@Slf4j
@Implementation
@RequiredArgsConstructor
public class PayUserValidator {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[0-9]{6}$");
    private final PasswordEncoder passwordEncoder;

    public void validatePassword(String payUserPassword, String inputPassword) {
        if (!PASSWORD_PATTERN.matcher(inputPassword).matches()) {
            throw new BusinessException(ExceptionMessage.INVALID_PASSWORD);
        }

        if (!passwordEncoder.matches(inputPassword, payUserPassword)) {
            throw new BusinessException(ExceptionMessage.INVALID_PASSWORD);
        }
    }
}
