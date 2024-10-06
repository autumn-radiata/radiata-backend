package radiata.service.user.api.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.domain.user.dto.request.UserCreateRequestDto;
import radiata.common.message.SuccessMessage;
import radiata.common.response.CommonResponse;
import radiata.common.response.SuccessResponse;
import radiata.service.user.core.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 사용자 회원 가입
     */
    @PostMapping("/sign-up")
    public CommonResponse createUser(@RequestBody UserCreateRequestDto request) {
        authService.registerUser(request);
        return SuccessResponse.success(SuccessMessage.OK.getMessage());
    }
}
