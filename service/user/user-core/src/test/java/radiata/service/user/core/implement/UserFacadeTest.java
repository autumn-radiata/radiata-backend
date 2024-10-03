package radiata.service.user.core.implement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import radiata.common.domain.user.dto.request.ModifyUserRequestDto;
import radiata.common.domain.user.dto.request.UserCreateRequestDto;
import radiata.common.domain.user.dto.response.GetUserInfoResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.user.core.domain.model.entity.User;
import radiata.service.user.core.service.Mapper.UserMapper;
import radiata.service.user.core.service.UserService;

@DisplayName("유저 퍼사이드 test")
@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserFacade userFacade;

    @Nested
    class RegisterUser {

        @Test
        @DisplayName("회원가입 테스트")
        public void testRegisterUser() {

            // given
            UserCreateRequestDto dto = new UserCreateRequestDto(
                "username", // 사용자 이름
                "password", // 비밀번호
                "test@example.com", // 이메일
                "nickname", // 닉네임
                "010-1234-5678", // 휴대폰 번호
                "도로명", // 도로명 주소
                "상세", // 상세 주소
                "우편" // 우편번호
            );

            when(userService.EncodePassword(anyString())).thenReturn("encodedPassword");

            // when
            userFacade.registerUser(dto);

            // then
            verify(userService, times(1)).validEmailUnique(dto.email());
            verify(userService, times(1)).EncodePassword(dto.password());
            verify(userService, times(1)).createUser(dto, "encodedPassword");
        }

        @Test
        @DisplayName("회원 가입 실패 시 이메일 중복 테스트")
        public void testRegisterUser_EmailDuplication() {
            // given
            UserCreateRequestDto dto = new UserCreateRequestDto(
                "username", // 사용자 이름
                "password", // 비밀번호
                "test@example.com", // 이메일
                "nickname", // 닉네임
                "010-1234-5678", // 휴대폰 번호
                "도로명", // 도로명 주소
                "상세", // 상세 주소
                "우편" // 우편번호
            );

            doThrow(new BusinessException(ExceptionMessage.USER_DUPLICATE_EMAIL)).when(userService)
                .validEmailUnique(dto.email());

            //when,then
            BusinessException exception = assertThrows(BusinessException.class, () -> {
                userFacade.registerUser(dto);
            });

            assertEquals("이메일이 중복 됩니다.", exception.getMessage());
            verify(userService, times(1)).validEmailUnique(dto.email());
            verify(userService, never()).EncodePassword(anyString());
            verify(userService, never()).createUser(any(), anyString());
        }


    }

    @Test
    @DisplayName("회원 정보 조회 테스트")
    public void testGetUserInfo() {
        // given
        String userId = "userId";
        User user = User.builder().build();
        GetUserInfoResponseDto dto = new GetUserInfoResponseDto(
            "username",
            "test@example.com",
            "nickname",
            "010-1234-5678",
            "도로명",
            "상세",
            "우편" );

        when(userService.findValidUser(userId)).thenReturn(user);
        when(userMapper.toGetUserInfoResponseDto(user)).thenReturn(dto);

        //when
        GetUserInfoResponseDto result = userFacade.getUserInfo(userId);

        //then
        verify(userService, times(1)).findValidUser(userId);
        assertEquals(dto, result);
    }


    @Test
    @DisplayName("회원 정보 수정 테스트")
    public void testUpdateUserInfo() {
        // given
        String userId = "user123";
        ModifyUserRequestDto dto = new ModifyUserRequestDto("newNickname", "newAddress","road","detail","zip");
        User user = mock(User.class);

        when(userService.findValidUser(userId)).thenReturn(user);

        // when
        userFacade.updateUserInfo(userId, dto);

        // then
        verify(userService, times(1)).findValidUser(userId);
        verify(user, times(1)).updateInfo(dto);
    }
}