package radiata.service.brand.core.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import radiata.service.brand.core.model.entity.Category;
import radiata.service.brand.core.model.repository.CategoryRepository;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, String>, CategoryRepository {

}
