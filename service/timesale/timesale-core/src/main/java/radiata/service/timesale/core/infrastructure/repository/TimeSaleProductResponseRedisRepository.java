package radiata.service.timesale.core.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TimeSaleProductResponseRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    public static final String productTimeSaleDefaultKey = "cacheProductTimeSales::";

    public void delete(String productId) {

        redisTemplate.delete(productTimeSaleDefaultKey + productId);
    }
}
