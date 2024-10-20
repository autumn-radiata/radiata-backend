package radiata.service.brand.core.implement;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import radiata.common.domain.brand.response.ProductGetResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private static final String PRODUCT_CACHE_NAME = "ProductCache::";
    private static final String PRODUCT_TIME_SALE_NAME_CACHE_NAME = "ProductTimeSaleCache::";
    private static final String PRODUCT_SEARCH_CACHE_NAME = "ProductSearchCache::";

    private final RedisRepository redisRepository;

    public <T> T getProductDto(String key, Class<T> clazz) {
        return redisRepository.getValueAsClass(key, clazz);
    }

    public void putExpireProductDto(String key, long timeOut) {
        redisRepository.setExpire(key, timeOut);
    }

    public void putProductDto(ProductGetResponseDto dto) {
        String key = PRODUCT_CACHE_NAME + dto.productId();
        String timeSaleKey = PRODUCT_TIME_SALE_NAME_CACHE_NAME + dto.productId();

        redisRepository.setIfPresent(key, dto);
        redisRepository.setIfPresent(timeSaleKey, dto);
    }

    public void setProductDtoWithExpire(String key, Object value, long timeout) {
        redisRepository.setValueWithExpire(key, value, timeout);
    }

    public void setProductDtoWithExpireAt(String key, Object value, LocalDateTime endTime) {
        redisRepository.setValue(key, value);
        redisRepository.setExpiredAt(key, endTime);
    }

    public void evictProduct(String productId) {
        String key = PRODUCT_CACHE_NAME + productId;
        String timeSaleKey = PRODUCT_TIME_SALE_NAME_CACHE_NAME + productId;

        redisRepository.deleteValue(key);
        redisRepository.deleteValue(timeSaleKey);
    }

    public void evictPagedProductsCache() {
        String productListKey = PRODUCT_SEARCH_CACHE_NAME + "*";
        Set<String> keys = redisRepository.hasKeys(productListKey);
        redisRepository.deleteValues(keys);
    }


}
