package radiata.service.user.core.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import radiata.service.user.core.domain.model.entity.PointHistory;
import radiata.service.user.core.domain.repository.PointHistoryRepository;

public interface PointHistoryJpaRepository extends PointHistoryRepository, JpaRepository<PointHistory,String> {

}
