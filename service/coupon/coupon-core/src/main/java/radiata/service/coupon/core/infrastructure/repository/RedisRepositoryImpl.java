package radiata.service.coupon.core.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import radiata.service.coupon.core.domain.repository.RedisRepository;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Long sAdd(String key, String value) {

        return redisTemplate.opsForSet().add(key, value);
    }

    @Override
    public Long sCard(String key) {

        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Boolean sIsMember(String key, String value) {

        return redisTemplate.opsForSet().isMember(key, value);
    }
}
