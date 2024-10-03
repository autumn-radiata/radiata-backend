package radiata.service.user.core.domain.repository;


public interface UserRepository {

    boolean existsByEmail(String email);

}
