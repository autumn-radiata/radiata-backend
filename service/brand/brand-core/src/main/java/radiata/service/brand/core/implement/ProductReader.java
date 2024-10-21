package radiata.service.brand.core.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.annotation.Implementation;
import radiata.common.domain.brand.request.ProductSearchCondition;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.brand.core.model.entity.Product;
import radiata.service.brand.core.model.repository.ProductQueryDslRepository;
import radiata.service.brand.core.model.repository.ProductRepository;

@Implementation
@RequiredArgsConstructor
public class ProductReader {

    private final ProductRepository productRepository;
    private final ProductQueryDslRepository productQueryDslRepository;

    public Product read(String productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new BusinessException(ExceptionMessage.PRODUCT_NOT_FOUND));
    }

    public Page<Product> readWithCondition(ProductSearchCondition condition, Pageable pageable) {
        return productQueryDslRepository.findProductsByCondition(condition, pageable);
    }
}
