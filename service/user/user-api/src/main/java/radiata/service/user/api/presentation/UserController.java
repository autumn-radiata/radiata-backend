package radiata.service.user.api.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.domain.user.dto.request.PointModifyRequestDto;
import radiata.common.domain.user.dto.request.UserModifyRequestDto;
import radiata.common.message.SuccessMessage;
import radiata.common.response.CommonResponse;
import radiata.common.response.SuccessResponse;
import radiata.service.user.core.service.UserCommandService;
import radiata.service.user.core.service.UserQueryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    /**
     * 사용자 정보 조회
     */
    @GetMapping
    public CommonResponse getUserInfo(@RequestHeader("X-UserId") String userId) {
        var response = userQueryService.getUserInfo(userId);
        return SuccessResponse.success(SuccessMessage.OK.getMessage(), response);
    }

    /**
     * 사용자 포인트 내역 조회
     */
    @GetMapping("/pointHistories")
    public CommonResponse getUserPointHistories(@RequestHeader("X-UserId") String userId,
        @PageableDefault(page = 0, size = 10, sort = "issueAt", direction = Direction.DESC) Pageable pageable
    ) {
        var response = userQueryService.getPointHistories(userId, pageable);
        return SuccessResponse.success(SuccessMessage.OK.getMessage(), response);
    }

    /**
     * 사용자 정보 수정
     */
    @PatchMapping
    public CommonResponse patchUser(@RequestHeader("X-UserId") String userId,
        @RequestBody UserModifyRequestDto request) {
        userCommandService.updateUserInfo(userId, request);
        return SuccessResponse.success(SuccessMessage.OK.getMessage());
    }

    /**
     * 사용자 탈퇴
     */
    @DeleteMapping
    public CommonResponse deleteUser(@RequestHeader("X-UserId") String userId) {
        userCommandService.removeUser(userId);
        return SuccessResponse.success(SuccessMessage.OK.getMessage());
    }

    /**
     * 포인트 차감
     */
    @PatchMapping("/points/deduct")
    public CommonResponse deductPoint(@RequestBody PointModifyRequestDto request) {
        userCommandService.deductPoint(request);
        return SuccessResponse.success(SuccessMessage.OK.getMessage());
    }
}
