package radiata.service.brand.core.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import radiata.service.brand.core.model.entity.Product;
import radiata.service.brand.core.model.repository.ProductRepository;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, String>, ProductRepository {

}
