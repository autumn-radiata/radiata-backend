package radiata.service.coupon.core.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CacheTestConfig {

    @Bean
    public CacheManager testCacheManager() {
        return new ConcurrentMapCacheManager();
    }
}
