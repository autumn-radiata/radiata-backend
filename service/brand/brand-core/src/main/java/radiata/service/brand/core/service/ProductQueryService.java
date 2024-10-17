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
        ProductGetResponseDto timeSaleProductDto = (ProductGetResponseDto) redisService.getValue(timeSaleKey);
        if (timeSaleProductDto != null) {
            log.info("레디스 조회:타임세일 상품  " + timeSaleKey);
            return timeSaleProductDto;
        }

        //2. 레디스 - 일반 상품 조회
        ProductGetResponseDto productDto = (ProductGetResponseDto) redisService.getValue(key);
        if (productDto != null) {
            log.info("레디스 조회:일반 상품  " + key);
            // TTL 갱신 - TTI
            redisService.setExpire(key, PRODUCT_EXPIRED_DURATION);
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
        redisService.setValueWithExpire(key, newProductDto, PRODUCT_EXPIRED_DURATION);
        return newProductDto;
    }

    /**
     * 상품 목록 조회
     */
    @Transactional(readOnly = true)
    public Page<ProductGetResponseDto> getProducts(ProductSearchCondition condition, Pageable pageable) {

        String productSearchPagingKey = PRODUCT_SEARCH_CACHE_NAME + pageable.getPageNumber();

        //1.레디스 데이터 조회
        Page<ProductGetResponseDto> productPaging =
            (Page<ProductGetResponseDto>) redisService.getValue(productSearchPagingKey);

        //2.db 데이터 조회
        //TimeSaleProduct api
        //최저가 비교
        //redis 저장
        return null;
    }


    private ProductGetResponseDto fetchAndCacheTimeSaleProduct(Product product, String key) {

        //todo : fallbackFactory 비즈니스로직 분리되어 유지보수성 떨어짐 다른 방법을 찾아보기
        try {
            TimeSaleProductResponseDto maxDisCountTimeSaleProduct =
                timeSaleClient.getMaxDiscountTimeSaleProduct(product.getId()).data();

            if (maxDisCountTimeSaleProduct.discountRate() > product.getDiscountAmount()) {
                product.setMaxDiscountAmount(maxDisCountTimeSaleProduct.discountRate());

                ProductGetResponseDto maxDisCountTimeSaleProductDto = productMapper.toProductGetResponseDto(product);
                redisService.setValueWithExpireAt(key, maxDisCountTimeSaleProductDto,
                    maxDisCountTimeSaleProduct.timeSaleEndTime());
                return maxDisCountTimeSaleProductDto;
            }
        } catch (FeignException.NotFound e) {
            log.info("세일 상품으로 등록되어 있지 않은 상품");
        }
        return null;
    }

}
