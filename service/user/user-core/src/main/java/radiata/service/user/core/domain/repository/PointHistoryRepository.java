package radiata.service.user.core.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.service.user.core.domain.model.entity.PointHistory;
import radiata.service.user.core.domain.model.entity.User;

public interface PointHistoryRepository {

    Page<PointHistory> findAllByUser(User user, Pageable pageable);
}
