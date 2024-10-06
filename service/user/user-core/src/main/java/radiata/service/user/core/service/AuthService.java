package radiata.service.user.core.service;

import com.github.ksuid.Ksuid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import radiata.common.domain.user.dto.request.UserCreateRequestDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.user.core.domain.model.constant.UserRole;
import radiata.service.user.core.domain.model.entity.User;
import radiata.service.user.core.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    public void registerUser(UserCreateRequestDto dto) {
        validEmailUnique(dto.email());
        String encodedPassword = EncodePassword(dto.password());
        createUser(dto, encodedPassword);
    }

    private void createUser(UserCreateRequestDto dto, String encodedPassword) {
        String id = Ksuid.newKsuid().toString();
        //todo :mapper로 변경?
        User user = User.of(
            id,
            encodedPassword,
            dto.email(),
            dto.nickname(),
            dto.phone(),
            dto.roadAddress(),
            dto.detailAddress(),
            dto.zipcode(),
            UserRole.CUSTOMER);
        userRepository.save(user);
    }

    private void validEmailUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ExceptionMessage.USER_DUPLICATE_EMAIL);
        }
    }

    private String EncodePassword(String password) {
        return passwordEncoder.encode(password);
    }


}
