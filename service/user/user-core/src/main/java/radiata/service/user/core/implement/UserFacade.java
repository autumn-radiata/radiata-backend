package radiata.service.user.core.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.user.dto.request.ModifyUserRequestDto;
import radiata.common.domain.user.dto.request.UserCreateRequestDto;
import radiata.common.domain.user.dto.response.GetUserInfoResponseDto;
import radiata.service.user.core.domain.model.entity.User;
import radiata.service.user.core.service.Mapper.UserMapper;
import radiata.service.user.core.service.UserService;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final UserMapper userMapper;

   /**
    * 회원가입
    */
   @Transactional
    public void registerUser(UserCreateRequestDto dto) {

        userService.validEmailUnique(dto.email());
        String encodedPassword  = userService.EncodePassword(dto.password());
        userService.createUser(dto,encodedPassword);
    }

    /**
     * 사용 로그인 : filter로 변경 -> security는 제일 마지막에 적용
     */

    public void test() {
        userService.testhi(); // userService의 testhi 메서드를 호출
    }

    /**
     * 회원 정보 조회
     */
    @Transactional(readOnly = true)
    public GetUserInfoResponseDto getUserInfo(String userId) {
        User user = userService.findValidUser(userId);
        return userMapper.userToGetUserInfoResponseDto(user);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public void updateUserInfo(String userId, ModifyUserRequestDto dto) {
        User user = userService.findValidUser(userId);
        user.updateInfo(dto);
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public void removeUser(String userId) {
        User user = userService.findValidUser(userId);
        user.delete(userId);
    }


}
