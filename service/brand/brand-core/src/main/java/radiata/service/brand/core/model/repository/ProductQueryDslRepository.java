package radiata.service.brand.core.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.brand.request.ProductSearchCondition;
import radiata.service.brand.core.model.entity.Product;

public interface ProductQueryDslRepository {

    Page<Product> findProductsByCondition(ProductSearchCondition condition, Pageable pageable);
}
