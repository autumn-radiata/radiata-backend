package radiata.service.brand.core.model.repository;

import java.util.Optional;
import radiata.service.brand.core.model.entity.Product;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(String id);


}
