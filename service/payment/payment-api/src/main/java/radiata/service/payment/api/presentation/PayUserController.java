package radiata.service.payment.api.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.domain.payment.dto.request.PayUserAddMoneyRequestDto;
import radiata.common.domain.payment.dto.request.PayUserCreateRequestDto;
import radiata.common.domain.payment.dto.request.PayUserSubtractMoneyRequestDto;
import radiata.common.domain.payment.dto.response.PayUserResponseDto;
import radiata.common.message.SuccessMessage;
import radiata.common.response.SuccessResponse;
import radiata.service.payment.core.service.PayUserService;

@Tag(name = "간편결제 사용자", description = "간편결제 사용자 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payusers")
public class PayUserController {

    private final PayUserService payUserService;

    @GetMapping()
    public SuccessResponse<PayUserResponseDto> getPayUser(@RequestHeader("X-UserId") String userId) {
        PayUserResponseDto response = payUserService.getPayUser(userId);
        return SuccessResponse.success(SuccessMessage.OK.getMessage(), response);
    }

    @PostMapping()
    public SuccessResponse<PayUserResponseDto> signupPayUser(
        @RequestHeader("X-UserId") String userId,
        @Validated @RequestBody PayUserCreateRequestDto request
    ) {
        PayUserResponseDto response = payUserService.signupPayUser(userId, request);
        return SuccessResponse.success(SuccessMessage.OK.getMessage(), response);
    }

    @PatchMapping("/money/add")
    public SuccessResponse<PayUserResponseDto> addMoney(
        @RequestHeader("X-UserId") String userId,
        @Validated @RequestBody PayUserAddMoneyRequestDto request
    ) {
        PayUserResponseDto response = payUserService.addMoney(userId, request);
        return SuccessResponse.success(SuccessMessage.OK.getMessage(), response);
    }

    @PatchMapping("/money/subtract")
    public SuccessResponse<PayUserResponseDto> subtractMoney(
        @RequestHeader("X-UserId") String userId,
        @Validated @RequestBody PayUserSubtractMoneyRequestDto request
    ) {
        PayUserResponseDto response = payUserService.subtractMoney(userId, request);
        return SuccessResponse.success(SuccessMessage.OK.getMessage(), response);
    }
}
