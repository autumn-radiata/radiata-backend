package radiata.service.user.core.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import radiata.service.user.core.domain.model.entity.PointHistory;

public interface PointHistoryRepository {

    Page<PointHistory> findAllById(String id, Pageable pageable);
}
