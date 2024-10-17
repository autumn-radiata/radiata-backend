package radiata.service.brand.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import radiata.common.domain.brand.response.ProductGetResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private static final String PRODUCT_CACHE_NAME = "ProductCache::";
    private static final String PRODUCT_TIME_SALE_NAME_CACHE_NAME = "ProductTimeSaleCache::";
    private static final String PRODUCT_SEARCH_CACHE_NAME = "ProductSearchCache::";
    private static final long PRODUCT_EXPIRED_DURATION = 20;
    private static final long PRODUCT_SEARCH_DURATION = 120;

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void evictProduct(String productId) {
        String key = PRODUCT_CACHE_NAME + productId;
        String timeSaleKey = PRODUCT_TIME_SALE_NAME_CACHE_NAME + productId;

        if (hasKey(timeSaleKey)) {
            deleteValue(timeSaleKey);
        } else if (hasKey(key)) {
            deleteValue(key);
        }
    }

    public void evictPagedProductsCache() {
        String productListKey = PRODUCT_SEARCH_CACHE_NAME + "*";
        Set<String> keys = hasKeys(productListKey);
        if (keys != null && !keys.isEmpty()) {
            deleteValues(keys);
        }
    }


    public void updateProduct(ProductGetResponseDto dto) {
        String key = PRODUCT_CACHE_NAME + dto.productId();
        String timeSaleKey = PRODUCT_TIME_SALE_NAME_CACHE_NAME + dto.productId();

        if (hasKey(timeSaleKey)) {
            setValue(timeSaleKey, dto);
        } else if (hasKey(key)) {
            setValue(key, dto);
        }
    }

    public void setValueWithExpireAt(String key, Object value, LocalDateTime endTime) {
        setValue(key, value);
        setExpiredAt(key, endTime);
    }

    public void setValueWithExpire(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }


    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public <T> T getValueAsClass(String key, Class<T> clazz) {
        return objectMapper.convertValue(redisTemplate.opsForValue().get(key), clazz);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    public Boolean setExpire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public Boolean setExpiredAt(String key, LocalDateTime endTime) {
        return redisTemplate.expireAt(key, endTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    //todo : o(n) 시간복잡도로 scan으로 변경 후에 비교
    public Set<String> hasKeys(String key) {
        return redisTemplate.keys(key);
    }

    public void deleteValues(Set<String> keys) {
        redisTemplate.delete(keys);
    }

}
