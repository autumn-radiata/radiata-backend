package radiata.service.brand.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.brand.response.ProductGetResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.brand.core.model.constant.ColorType;
import radiata.service.brand.core.model.constant.GenderType;
import radiata.service.brand.core.model.constant.SizeType;
import radiata.service.brand.core.model.entity.Brand;
import radiata.service.brand.core.model.entity.Category;
import radiata.service.brand.core.model.entity.Product;
import radiata.service.brand.core.model.repository.BrandRepository;
import radiata.service.brand.core.model.repository.CategoryRepository;
import radiata.service.brand.core.model.repository.ProductQueryDslRepository;
import radiata.service.brand.core.model.repository.ProductRepository;
import radiata.service.brand.core.service.Mapper.ProductMapper;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductQueryService {

    private static final String PRODUCT_CACHE_NAME = "cacheProduct";
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductQueryDslRepository productQueryDslRepository;
    private final ProductMapper productMapper;

    /**
     * 상품 상세 조회
     */
    @Cacheable(cacheNames = PRODUCT_CACHE_NAME, key = "args[0]")
    public ProductGetResponseDto getProduct(String productId) {
        log.info("product read One: " + productId);

        Product product = findValidProduct(productId);
        /*
        // Redis의 타임세일 할인 정보와 비교, 최저가 반환
        Integer maxDiscount = getMaxDiscount(product.getId(), product.getDiscountAmount());
        product.setLowestDiscountAmount(maxDiscount);
        */
        return productMapper.toProductGetResponseDto(product);
    }

    /**
     * 상품 목록 조회
     */
    //todo : 레디스에 페이징 적재는 더 생각해보기
    public Page<ProductGetResponseDto> getProducts(String brandId, String categoryId, GenderType gender, SizeType size,
        ColorType color, String query, Pageable pageable) {
        Brand brand = findValidBrand(brandId);
        Category category = findValidCategory(categoryId);
        Page<Product> list = productQueryDslRepository.findProductsByCondition(brand, category, gender, size, color,
            query, pageable);
        return list.map(productMapper::toProductGetResponseDto);
    }

    private Brand findValidBrand(String brandId) {
        return brandRepository.findById(brandId)
            .orElseThrow(() -> new BusinessException(ExceptionMessage.BRAND_NOT_FOUND));
    }

    private Category findValidCategory(String categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new BusinessException(ExceptionMessage.CATEGORY_NOT_FOUND));
    }

    private Product findValidProduct(String productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new BusinessException(ExceptionMessage.PRODUCT_NOT_FOUND));
    }

}
