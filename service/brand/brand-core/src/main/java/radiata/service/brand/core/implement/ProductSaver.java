package radiata.service.brand.core.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.annotation.Implementation;
import radiata.service.brand.core.model.entity.Product;
import radiata.service.brand.core.model.repository.ProductRepository;

@Implementation
@RequiredArgsConstructor
@Transactional
public class ProductSaver {

    private final ProductRepository productRepository;

    public void save(Product product) {
        productRepository.save(product);
    }


}
