package radiata.service.brand.api.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import radiata.service.brand.core.service.BrandService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;

    /**
     * 브랜드 생성
     */
    @PostMapping
    public void createBrand() {
        brandService.create();
    }
}
