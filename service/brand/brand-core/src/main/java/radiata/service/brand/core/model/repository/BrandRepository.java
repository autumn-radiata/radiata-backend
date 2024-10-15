package radiata.service.brand.core.model.repository;

import java.util.Optional;
import radiata.service.brand.core.model.entity.Brand;

public interface BrandRepository {

    Brand save(Brand brand);

    Optional<Brand> findById(String id);
}
