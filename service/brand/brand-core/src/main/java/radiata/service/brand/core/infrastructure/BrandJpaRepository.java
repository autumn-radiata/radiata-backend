package radiata.service.brand.core.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import radiata.service.brand.core.model.entity.Brand;
import radiata.service.brand.core.model.repository.BrandRepository;

@Repository
public interface BrandJpaRepository extends JpaRepository<Brand, String>, BrandRepository {

}
