package radiata.service.user.core.implement;

import com.github.ksuid.Ksuid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.service.user.core.domain.model.constant.PointType;
import radiata.service.user.core.domain.model.entity.PointHistory;
import radiata.service.user.core.domain.model.entity.User;
import radiata.service.user.core.service.PointService;
import radiata.service.user.core.service.UserService;

@Service
@RequiredArgsConstructor
public class PointFacade {

    private final UserService userService;
    private final PointService pointService;

    /**
     * 포인트 정산 (증감,차감)
     */
    @Transactional
    public void adjustPoint(String userId, int PointAmount, PointType type) {
        User user = userService.findValidUser(userId);
        pointService.calculatePoint(user,PointAmount,type);
    }

    /**
     * 포인트 내역 조회
     */
    @Transactional(readOnly = true)
    public Set<PointHistory> getPointHistories(String userId) {
        User user=userService.findValidUser(userId);
        return user.getPointHistories();
    }


}
