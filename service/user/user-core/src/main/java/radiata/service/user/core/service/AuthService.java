package radiata.service.user.core.service;

import com.github.ksuid.Ksuid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import radiata.common.domain.user.constant.UserRole;
import radiata.common.domain.user.dto.request.UserCreateRequestDto;
import radiata.common.domain.user.dto.request.UserLoginRequestDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.user.core.domain.model.entity.User;
import radiata.service.user.core.domain.repository.UserRepository;
import radiata.service.user.core.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입
     */
    public void registerUser(UserCreateRequestDto dto, UserRole userRole) {
        validEmailUnique(dto.email());
        String encodedPassword = encodePassword(dto.password());
        createUser(dto, encodedPassword, userRole);
    }

    /**
     * 로그인
     */
    public String loginUser(UserLoginRequestDto dto) {
        User user = findValidUserByEmail(dto.email());
        checkPasswordMatch(user, dto.password());
        return jwtUtil.createToken(user.getId(), user.getRole());
    }

    private void checkPasswordMatch(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ExceptionMessage.USER_NOT_FOUND);
        }
    }

    private void createUser(UserCreateRequestDto dto, String encodedPassword, UserRole userRole) {
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
            userRole);
        userRepository.save(user);
    }


    private void validEmailUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ExceptionMessage.USER_DUPLICATE_EMAIL);
        }
    }

    private User findValidUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(ExceptionMessage.USER_NOT_FOUND));
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


}
