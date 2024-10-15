package radiata.service.user.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.user.dto.request.PointModifyRequestDto;
import radiata.common.domain.user.dto.request.UserModifyRequestDto;
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
    private final PointHandler pointHandler;

    /**
     * 회원 정보 수정
     */
    public void updateUserInfo(String userId, UserModifyRequestDto dto) {
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
     * 포인트 차감
     */
    public void deductPoint(PointModifyRequestDto dto) {
        User user = findValidUser(dto.userId());
        user.deductPoint(dto.point());
        pointHandler.recordPointHistory(user, dto.point(), PointType.SUBSCRIBE);
    }

    private User findValidUser(String userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ExceptionMessage.USER_NOT_FOUND));
    }

}
