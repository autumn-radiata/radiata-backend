package radiata.service.user.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.user.dto.request.ModifyUserRequestDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.user.core.domain.model.constant.PointType;
import radiata.service.user.core.domain.model.entity.User;
import radiata.service.user.core.domain.repository.UserRepository;
import radiata.service.user.core.implement.PointHandler;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private PointHandler pointHandler;

    /**
     * 회원 정보 수정
     */
    public void updateUserInfo(String userId, ModifyUserRequestDto dto) {
        User user = findValidUser(userId);
        user.updateInfo(
            dto.nickname(),
            dto.phone(),
            dto.roadAddress(),
            dto.detailAddress(),
            dto.zipcode()
        );
    }

    /**
     * 회원 삭제
     */
    public void removeUser(String userId) {
        User user = findValidUser(userId);
        user.delete(userId);
    }


    /**
     * 포인트 정산 (증감,차감)
     */
    public void adjustPoint(String userId, int PointAmount, PointType type) {
        User user = findValidUser(userId);
        pointHandler.calculatePoint(user, PointAmount, type);
    }

    private User findValidUser(String userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ExceptionMessage.USER_NOT_FOUND));
    }

}
