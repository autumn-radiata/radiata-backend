package radiata.service.brand.core.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import radiata.common.annotation.Implementation;

@Implementation
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T getValueAsClass(String key, Class<T> clazz) {
        return objectMapper.convertValue(redisTemplate.opsForValue().get(key), clazz);
    }

    public void setValueWithExpire(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setIfPresent(String key, Object value) {
        redisTemplate.opsForValue().setIfPresent(key, value);
    }

    public void setExpire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public void setExpiredAt(String key, LocalDateTime endTime) {
        redisTemplate.expireAt(key, endTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    //todo : o(n) 시간복잡도로 scan으로 변경 후에 비교
    public Set<String> hasKeys(String key) {
        return redisTemplate.keys(key);
    }

    public void deleteValue(String key) {
        redisTemplate.unlink(key);
    }

    public void deleteValues(Set<String> keys) {
        redisTemplate.unlink(keys);
    }

}
