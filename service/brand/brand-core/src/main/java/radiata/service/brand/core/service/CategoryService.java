package radiata.service.brand.core.service;

import com.github.ksuid.Ksuid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import radiata.service.brand.core.model.entity.Category;
import radiata.service.brand.core.model.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void createCategory() {
        String id = Ksuid.newKsuid().toString();

        categoryRepository.save(Category.of(id,
            "nameTest",
            "100",
            "100"));
    }

}
