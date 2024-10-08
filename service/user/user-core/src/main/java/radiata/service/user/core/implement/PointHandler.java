package radiata.service.user.core.implement;

import com.github.ksuid.Ksuid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import radiata.service.user.core.domain.model.constant.PointType;
import radiata.service.user.core.domain.model.entity.PointHistory;
import radiata.service.user.core.domain.model.entity.User;

@RequiredArgsConstructor
@Component
public class PointHandler {

    public void calculatePoint(User user, int pointAmount, PointType type) {
        if (type == PointType.SUBSCRIBE) {
            decreadPoint(user, pointAmount);
        } else {
            increadPoint(user, pointAmount, type);
        }
        recordPointHistory(user, pointAmount, type);
    }

    //todo : 카프카 batch로 변경 후 수정
    private void increadPoint(User user, int addPoint, PointType type) {
        user.getTotalPoint().addPoint(addPoint);
    }

    private void decreadPoint(User user, int subPoint) {
        user.getTotalPoint().subPoint(subPoint);
    }

    private void recordPointHistory(User user, int pointAmount, PointType type) {
        PointHistory newPointHistory = PointHistory.of(
            Ksuid.newKsuid().toString(),
            user,
            pointAmount,
            type
        );
        user.addPointHistory(newPointHistory);
    }
}
