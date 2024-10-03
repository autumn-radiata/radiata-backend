package radiata.service.user.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import radiata.common.domain.user.dto.request.UserCreateRequestDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.user.core.domain.model.constant.UserRole;
import radiata.service.user.core.domain.model.entity.User;
import radiata.service.user.core.infrastructure.UserJpaRepository;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void testhi() {

    }

    public void createUser(UserCreateRequestDto dto,String password) {
        User user = User.of(dto, UserRole.CUSTOMER);
        user.encoder(password);
        userRepository.save(user);
    }

    public User findValidUser(String userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ExceptionMessage.USER_NOT_FOUND));
    }

    public void validEmailUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ExceptionMessage.USER_DUPLICATE_EMAIL);
        }
    }

    public String EncodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
