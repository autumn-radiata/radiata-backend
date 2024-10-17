package radiata.service.brand.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.brand.request.ProductCreateRequestDto;
import radiata.common.domain.brand.request.ProductDeductRequestDto;
import radiata.common.domain.brand.response.ProductGetResponseDto;
import radiata.service.brand.core.implement.BrandReader;
import radiata.service.brand.core.implement.CategoryReader;
import radiata.service.brand.core.implement.ProductIdCreator;
import radiata.service.brand.core.implement.ProductReader;
import radiata.service.brand.core.implement.ProductSaver;
import radiata.service.brand.core.implement.ProductValidator;
import radiata.service.brand.core.model.constant.ColorType;
import radiata.service.brand.core.model.constant.GenderType;
import radiata.service.brand.core.model.constant.SizeType;
import radiata.service.brand.core.model.entity.Brand;
import radiata.service.brand.core.model.entity.Category;
import radiata.service.brand.core.model.entity.Product;
import radiata.service.brand.core.service.Mapper.ProductMapper;

@Service
@RequiredArgsConstructor
@Slf4j

public class ProductCommandService {

    private static final String PRODUCT_CACHE_NAME = "ProductCache";
    private static final String PRODUCT_LIST_CACHE_NAME = "ProductSearchCache";
    private final ProductValidator productValidator;
    private final ProductIdCreator productIdCreator;
    private final ProductSaver productSaver;
    private final ProductReader productReader;
    private final BrandReader brandReader;
    private final CategoryReader categoryReader;
    private final ProductMapper productMapper;

    /**
     * 상품 생성
     */
    @Transactional
    public ProductGetResponseDto createProduct(ProductCreateRequestDto dto) {
        productValidator.checkPriceAboveDiscountedAmount(dto.price(), dto.discountAmount());
        Brand brand = brandReader.read(dto.brandId());
        Category category = categoryReader.read(dto.categoryId());
        String id = productIdCreator.create();

        Product product = Product.of(id, brand, category, dto.name(), dto.price(), dto.discountAmount(),
            dto.stock(), GenderType.valueOf(dto.gender()), ColorType.valueOf(dto.color()),
            SizeType.valueOf(dto.size()));
        productSaver.save(product);
        return productMapper.toProductGetResponseDto(product);
    }

    /**
     * 상품 재고 차감
     */
    @Transactional
    @CachePut(cacheNames = PRODUCT_CACHE_NAME, key = "#result.productId")
    public ProductGetResponseDto deductInventory(ProductDeductRequestDto dto) {
        Product product = productReader.read(dto.productId());
        product.subStock(dto.quantity());
        return productMapper.toProductGetResponseDto(product);
    }

    /**
     * 상품 재고 증감 - 보상 트랜잭션 복구
     */
    @Transactional
    @CachePut(cacheNames = PRODUCT_CACHE_NAME, key = "#result.productId")
    public ProductGetResponseDto increaseInventory(ProductDeductRequestDto dto) {
        Product product = productReader.read(dto.productId());
        product.addStock(dto.quantity());
        return productMapper.toProductGetResponseDto(product);
    }

    /**
     * 상품 삭제
     */
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = PRODUCT_CACHE_NAME, key = "args[0]"),
        @CacheEvict(cacheNames = PRODUCT_LIST_CACHE_NAME, allEntries = true)
    })
    public void removeProduct(String productId) {
        Product product = productReader.read(productId);
        product.delete();
    }

    /**
     * 상품 수정
     */


}


