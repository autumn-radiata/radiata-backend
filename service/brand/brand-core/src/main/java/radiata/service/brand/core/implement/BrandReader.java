package radiata.service.brand.core.implement;

import lombok.RequiredArgsConstructor;
import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.brand.core.model.entity.Brand;
import radiata.service.brand.core.model.repository.BrandRepository;

@Implementation
@RequiredArgsConstructor
public class BrandReader {

    private final BrandRepository brandRepository;

    public Brand read(String brandId) {
        return brandRepository.findById(brandId)
            .orElseThrow(() -> new BusinessException(ExceptionMessage.BRAND_NOT_FOUND));
    }

}
