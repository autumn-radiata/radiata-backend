package radiata.service.brand.core.model.repository;

import java.util.Optional;
import radiata.service.brand.core.model.entity.Category;

public interface CategoryRepository {

    Optional<Category> findById(String id);

    Category save(Category category);
}
