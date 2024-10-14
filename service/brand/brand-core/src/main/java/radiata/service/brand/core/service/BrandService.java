package radiata.service.brand.core.service;

import com.github.ksuid.Ksuid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import radiata.service.brand.core.model.entity.Brand;
import radiata.service.brand.core.model.repository.BrandRepository;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandJpaRepository;

    public void create() {
        String id = Ksuid.newKsuid().toString();
        brandJpaRepository.save(Brand.of(id, "nameTest"));
    }

}
