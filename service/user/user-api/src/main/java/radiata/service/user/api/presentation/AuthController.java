package radiata.service.user.api.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.domain.user.constant.UserRole;
import radiata.common.domain.user.dto.request.UserCreateRequestDto;
import radiata.common.domain.user.dto.request.UserLoginRequestDto;
import radiata.common.message.SuccessMessage;
import radiata.common.response.CommonResponse;
import radiata.common.response.SuccessResponse;
import radiata.service.user.core.service.AuthService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 사용자 회원 가입
     */
    @PostMapping("/sign-up")
    public CommonResponse createUser(@RequestBody UserCreateRequestDto request) {
        authService.registerUser(request, UserRole.CUSTOMER);
        return SuccessResponse.success(SuccessMessage.OK.getMessage());
    }

    /**
     * 브랜드 운영자 회원가입
     */
    @PostMapping("/owner/sign-up")
    public CommonResponse createOwnerUser(@RequestBody UserCreateRequestDto request) {
        authService.registerUser(request, UserRole.BRAND_OWNER);
        return SuccessResponse.success(SuccessMessage.OK.getMessage());
    }

    /**
     * 사용자 로그인
     */
    @PostMapping("/sign-in")
    public CommonResponse loginUser(@RequestBody UserLoginRequestDto request) {
        var response = authService.loginUser(request);
        return SuccessResponse.success(SuccessMessage.OK.getMessage(), response);
    }
}
