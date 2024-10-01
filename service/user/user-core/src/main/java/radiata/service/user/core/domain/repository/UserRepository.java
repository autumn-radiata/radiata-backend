package radiata.service.user.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import radiata.service.user.core.domain.entity.User;

public interface UserRepository extends JpaRepository<User,String> {

}
