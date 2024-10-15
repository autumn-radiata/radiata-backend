package radiata.service.timesale.core.infrastructure.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import radiata.common.domain.timesale.dto.response.TimeSaleProductResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;

@Repository
@RequiredArgsConstructor
public class TimeSaleProductResponseRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final String productTimeSaleDefaultKet = "cacheProductTimeSales:";

    public void save(TimeSaleProductResponseDto responseDto, String productId) {

        try {
            String jsonValue = objectMapper.writeValueAsString(responseDto);
            Long ttl = Duration.between(LocalDateTime.now(), responseDto.timeSaleEndTime())
                    .toMillis();

            redisTemplate.opsForValue().set(productTimeSaleDefaultKet + productId, jsonValue, ttl,
                    TimeUnit.MICROSECONDS);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ExceptionMessage.SYSTEM_ERROR);
        }
    }

    public void delete(String productId) {

        redisTemplate.delete(productTimeSaleDefaultKet + productId);
    }
}
