package radiata.service.brand.core.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.service.brand.core.model.constant.ColorType;
import radiata.service.brand.core.model.constant.GenderType;
import radiata.service.brand.core.model.constant.SizeType;
import radiata.service.brand.core.model.entity.Brand;
import radiata.service.brand.core.model.entity.Category;
import radiata.service.brand.core.model.entity.Product;

public interface ProductQueryDslRepository {

    Page<Product> findProductsByCondition(Brand brand, Category category, GenderType gender, SizeType size,
        ColorType color, String query, Pageable pageable);
}
