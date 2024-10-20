package radiata.service.brand.core.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.brand.request.ProductSearchCondition;
import radiata.common.domain.brand.response.ProductGetResponseDto;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;
import radiata.service.brand.core.implement.ProductReader;
import radiata.service.brand.core.implement.RedisService;
import radiata.service.brand.core.model.entity.Product;
import radiata.service.brand.core.service.FeignClient.TimeSaleClient;
import radiata.service.brand.core.service.Mapper.ProductMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductQueryService {

    private static final String PRODUCT_CACHE_NAME = "ProductCache::";
    private static final String PRODUCT_TIME_SALE_NAME_CACHE_NAME = "ProductTimeSaleCache::";
    private static final String PRODUCT_SEARCH_CACHE_NAME = "ProductSearchCache::";
    private static final long PRODUCT_EXPIRED_DURATION = 20;
    private static final long PRODUCT_SEARCH_DURATION = 120;
    private final ProductReader productReader;
    private final ProductMapper productMapper;
    private final TimeSaleClient timeSaleClient;
    private final RedisService redisService;

    /**
     * 상품 상세 조회
     */
    @Transactional(readOnly = true)
    public ProductGetResponseDto getProduct(String productId) {

        String key = PRODUCT_CACHE_NAME + productId;
        String timeSaleKey = PRODUCT_TIME_SALE_NAME_CACHE_NAME + productId;

        //todo : 레디스 갱신 확인
        //1,2번의 경우 타임세일 상품이 갱신되면 레디스의 해당 데이터를 삭제.

        //1. 레디스 - 타임 세일 상품 조회
        ProductGetResponseDto timeSaleProductDto = redisService.getProductDto(timeSaleKey, ProductGetResponseDto.class);
        if (timeSaleProductDto != null) {
            log.info("레디스 조회:타임세일 상품  " + timeSaleKey);
            return timeSaleProductDto;
        }

        //2. 레디스 - 일반 상품 조회
        ProductGetResponseDto productDto = redisService.getProductDto(key, ProductGetResponseDto.class);
        if (productDto != null) {
            log.info("레디스 조회:일반 상품  " + key);
            redisService.putExpireProductDto(key, PRODUCT_EXPIRED_DURATION);
            return productDto;
        }

        Product product = productReader.read(productId);

        //3. db 조회 - 타임 세일 상품으로 레디스에 저장
        ProductGetResponseDto newTimeSaleProductDto = fetchAndCacheTimeSaleProduct(product, timeSaleKey);
        if (newTimeSaleProductDto != null) {
            log.info("db 조회:타임세일 상품  " + timeSaleKey);
            return newTimeSaleProductDto;
        }

        //4. db 조회 - 일반 상품으로 레디스에 저장
        log.info("db 조회:일반 상품  " + key);
        ProductGetResponseDto newProductDto = productMapper.toProductGetResponseDto(product);
        redisService.setProductDtoWithExpire(key, newProductDto, PRODUCT_EXPIRED_DURATION);
        return newProductDto;
    }

    /**
     * 상품 목록 조회
     */
    @Transactional(readOnly = true)
    public Page<ProductGetResponseDto> getProducts(ProductSearchCondition condition, Pageable pageable) {

        String productSearchPagingKey = generateSearchCacheKey(condition, pageable);
        Page<ProductGetResponseDto> cachedProducts = redisService.getProductDto(productSearchPagingKey, Page.class);
        if (cachedProducts != null) {
            return cachedProducts;
        }

        Page<ProductGetResponseDto> productsSearchWithCondition = productReader.readWithCondition(condition, pageable)
            .map(productMapper::toProductGetResponseDto);

        //todo : TimeSaleProduct api ,최저가 비교

        if (pageable.getPageSize() <= 5) {
            redisService.setProductDtoWithExpire(productSearchPagingKey, productsSearchWithCondition,
                PRODUCT_SEARCH_DURATION);
        }

        return productsSearchWithCondition;

    }


    private ProductGetResponseDto fetchAndCacheTimeSaleProduct(Product product, String key) {

        try {
            TimeSaleProductResponseDto maxDisCountTimeSaleProduct =
                timeSaleClient.getMaxDiscountTimeSaleProduct(product.getId()).data();

            if (maxDisCountTimeSaleProduct.discountRate() > product.getDiscountAmount()) {
                product.setMaxDiscountAmount(maxDisCountTimeSaleProduct.discountRate());

                ProductGetResponseDto maxDisCountTimeSaleProductDto = productMapper.toProductGetResponseDto(product);
                redisService.setProductDtoWithExpireAt(key, maxDisCountTimeSaleProductDto,
                    maxDisCountTimeSaleProduct.timeSaleEndTime());
                return maxDisCountTimeSaleProductDto;
            }
        } catch (FeignException.NotFound e) {
            log.info("세일 상품으로 등록되어 있지 않은 상품");
        }
        return null;
    }

    private String generateSearchCacheKey(ProductSearchCondition condition, Pageable pageable) {
        return String.format(PRODUCT_SEARCH_CACHE_NAME + "%s:%s:%s:%s:%s:%s:page:%d:size:%d",
            condition.brandId(),
            condition.categoryId(),
            condition.gender(),
            condition.size(),
            condition.color(),
            condition.query(),
            pageable.getPageNumber(),
            pageable.getPageSize());
    }

}
