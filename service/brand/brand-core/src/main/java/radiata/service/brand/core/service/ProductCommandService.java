package radiata.service.brand.core.service;

import com.github.ksuid.Ksuid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import radiata.common.domain.brand.request.ProductCreateRequestDto;
import radiata.common.domain.brand.request.ProductDeductRequestDto;
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
import radiata.service.brand.core.model.repository.ProductRepository;
import radiata.service.brand.core.service.Mapper.ProductMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCommandService {

    private static final String PRODUCT_CACHE_NAME = "cacheProduct";
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    /**
     * 상품 생성
     */
    public void createProduct(ProductCreateRequestDto dto) {
        Brand brand = findValidBrand(dto.brandId());
        Category category = findValidCategory(dto.categoryId());
        String id = createId();
        Product product = Product.of(id, brand, category, dto.name(), dto.price(), dto.discountAmount(),
            dto.stock(), GenderType.valueOf(dto.gender()), ColorType.valueOf(dto.color()),
            SizeType.valueOf(dto.size()));
        productRepository.save(product);
    }

    /**
     * 상품 재고 차감
     */
    @CachePut(cacheNames = PRODUCT_CACHE_NAME, key = "#result.productId")
    public ProductGetResponseDto deductInventory(ProductDeductRequestDto dto) {
        Product product = findValidProduct(dto.productId());
        product.subStock(dto.quantity());
        return productMapper.toProductGetResponseDto(product);
    }

    /**
     * 상품 재고 증감 - 보상 트랜잭션 복구
     */
    @CachePut(cacheNames = PRODUCT_CACHE_NAME, key = "#result.productId")
    public ProductGetResponseDto increaseInventory(String productId, Integer quantity) {
        Product product = findValidProduct(productId);
        product.addStock(quantity);
        return productMapper.toProductGetResponseDto(product);
    }


    /**
     * 상품 삭제
     */
    @Caching(evict = {
        @CacheEvict(cacheNames = PRODUCT_CACHE_NAME, key = "args[0]"),
        @CacheEvict(cacheNames = "storeAllCache", allEntries = true)
    })
    public void removeProduct(String productId) {
        Product product = findValidProduct(productId);
        product.delete();
    }

    /**
     * 상품 수정
     */

    private String createId() {
        return Ksuid.newKsuid().toString();
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
