package radiata.service.brand.core.service;

import feign.FeignException;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.brand.request.ProductSearchCondition;
import radiata.common.domain.brand.response.ProductGetResponseDto;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;
import radiata.service.brand.core.implement.BrandReader;
import radiata.service.brand.core.implement.CategoryReader;
import radiata.service.brand.core.implement.ProductReader;
import radiata.service.brand.core.model.entity.Product;
import radiata.service.brand.core.service.FeignClient.TimeSaleClient;
import radiata.service.brand.core.service.Mapper.ProductMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductQueryService {

    private static final String PRODUCT_CACHE_NAME = "cacheProduct";
    private final BrandReader brandReader;
    private final CategoryReader categoryReader;
    private final ProductReader productReader;
    private final ProductMapper productMapper;
    private final TimeSaleClient timeSaleClient;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 상품 상세 조회
     */
    //todo : redis 분리
    @Transactional(readOnly = true)
    //@Cacheable(cacheNames = PRODUCT_CACHE_NAME, key = "args[0]")
    public ProductGetResponseDto getProduct(String productId) {
        log.info("product read One: " + productId);

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Product product = productReader.read(productId);

        String key = "CacheProduct:" + productId;
        String timeSaleKey = "CacheTimeSaleProduct:" + productId;

        //캐시된 데이터 확인
        ProductGetResponseDto cachedProduct = (ProductGetResponseDto) ops.get(key);
        ProductGetResponseDto cachedTimeSaleProduct = (ProductGetResponseDto) ops.get(timeSaleKey);
        //todo : 타임 세일 상품 등록시 캐시 삭제 추가 후 캐시 데이터 사용
        if (cachedTimeSaleProduct != null) {
            log.info("타임세일상품: " + cachedTimeSaleProduct);
            return cachedTimeSaleProduct; // 캐시된 데이터가 존재하면 반환
        }
        if (cachedProduct != null) {
            log.info("상품: " + cachedProduct);
            return cachedProduct; // 캐시된 데이터가 존재하면 반환
        }

        ProductGetResponseDto dto;
        //todo : fallbackFactory로 분리
        try {
            TimeSaleProductResponseDto masDisCountTimeSaleProduct = timeSaleClient.getMaxDiscountTimeSaleProduct(
                productId).data();
            
            if (masDisCountTimeSaleProduct.discountRate() > product.getDiscountAmount()) {
                product.setMaxDiscountAmount(masDisCountTimeSaleProduct.discountRate());
                dto = productMapper.toProductGetResponseDto(product);
                ops.set(timeSaleKey, dto);
                redisTemplate.expireAt(timeSaleKey,
                    Date.from(masDisCountTimeSaleProduct.timeSaleEndTime().atZone(ZoneId.systemDefault()).toInstant()));
                return dto;
            }
        } catch (FeignException.NotFound e) {
            log.info("세일 상품으로 등록되어 있지 않은 상품");
        }
        // 일반 상품인 경우 기본값으로 설정
        dto = productMapper.toProductGetResponseDto(product);
        ops.set(key, dto);

        return productMapper.toProductGetResponseDto(product);
    }

    /**
     * 상품 목록 조회
     */
    //ttl,tti설정 1~3페이지의 데이터만 redis에 적재
    @Transactional(readOnly = true)
    public Page<ProductGetResponseDto> getProducts(ProductSearchCondition condition, Pageable pageable) {

        Page<Product> list = productReader.readWithCondition(condition, pageable);
        return list.map(productMapper::toProductGetResponseDto);
    }

}
