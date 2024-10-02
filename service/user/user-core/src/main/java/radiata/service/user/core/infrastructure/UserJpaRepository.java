package radiata.service.user.core.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import radiata.service.user.core.domain.model.entity.User;
import radiata.service.user.core.domain.repository.UserRepository;

public interface UserJpaRepository extends UserRepository, JpaRepository<User,String> {

}
