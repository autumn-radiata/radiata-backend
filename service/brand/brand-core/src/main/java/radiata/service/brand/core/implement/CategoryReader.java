package radiata.service.brand.core.implement;

import lombok.RequiredArgsConstructor;
import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.brand.core.model.entity.Category;
import radiata.service.brand.core.model.repository.CategoryRepository;

@Implementation
@RequiredArgsConstructor
public class CategoryReader {

    private final CategoryRepository categoryRepository;

    public Category read(String categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new BusinessException(ExceptionMessage.CATEGORY_NOT_FOUND));

    }

}
