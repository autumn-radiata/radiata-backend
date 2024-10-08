package radiata.service.user.core.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import radiata.service.user.core.domain.model.entity.User;
import radiata.service.user.core.domain.repository.UserRepository;

@Repository
public interface UserJpaRepository extends JpaRepository<User,String>,UserRepository {
}
