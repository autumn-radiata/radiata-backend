package radiata.service.user.core.domain.repository;


import java.util.Optional;
import radiata.service.user.core.domain.model.entity.User;

public interface UserRepository {

    Optional<User> findById(String id);

    User save(User user);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
