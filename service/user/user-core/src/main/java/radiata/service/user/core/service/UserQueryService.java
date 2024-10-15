package radiata.service.user.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.user.dto.response.PointHistoryGetResponseDto;
import radiata.common.domain.user.dto.response.UserGetInfoResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.user.core.domain.model.entity.User;
import radiata.service.user.core.domain.repository.PointHistoryRepository;
import radiata.service.user.core.domain.repository.UserRepository;
import radiata.service.user.core.service.Mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserMapper userMapper;

    /**
     * 회원 정보 조회
     */
    @Transactional(readOnly = true)
    public UserGetInfoResponseDto getUserInfo(String userId) {
        var user = findValidUser(userId);
        return userMapper.userToUserGetInfoResponseDto(user);
    }

    /**
     * 포인트 내역 조회
     */
    @Transactional(readOnly = true)
    public Page<PointHistoryGetResponseDto> getPointHistories(String userId, Pageable pageable) {
        User user = findValidUser(userId);
        var pointHistories = pointHistoryRepository.findAllByUser(user, pageable);
        return pointHistories.map(userMapper::userToPointHistoriesGetResponseDto);

    }

    private User findValidUser(String userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ExceptionMessage.USER_NOT_FOUND));
    }

}
