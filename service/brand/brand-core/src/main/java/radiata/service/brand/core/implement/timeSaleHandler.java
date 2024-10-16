package radiata.service.brand.core.implement;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import radiata.service.brand.core.service.FeignClient.TimeSaleClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class timeSaleHandler {

    private static final String TIME_SALE_CACHE_NAME = "cacheTimeSale:";
    private RedisTemplate<String, Object> redisTemplate;
    private TimeSaleClient timeSaleClient;

    //오전12시 타임세일에 대한 데이터를 요청
    @Scheduled()
    private void updateTimeSales() {
        log.info("스케줄러 시작");
        /*ZSetOperations<String, Object> ops = redisTemplate.opsForZSet();

        //feignClient 타임세일 상품의 데이터 요청
        List<TimeSaleProductGetResponseDto> timeSales = timeSaleClient.getProducts();

        // Redis에 타임세일 할인 정보를 적재
        timeSales.forEach(timeSale -> {
            System.out.println("endTime:[" + timeSale.productId() + "] :" + timeSale.timeSaleEndTime());
            String redisKey = TIME_SALE_CACHE_NAME + timeSale.productId();
            Integer discountAmount = timeSale.discountRate();
            long startEpoch = timeSale.timeSaleStartTime().toInstant(ZoneOffset.UTC).getEpochSecond();

            ops.add(redisKey, discountAmount, startEpoch);

            // LocalDateTime을 Date로 변환하여 Redis에 만료 시간 설정
            redisTemplate.expireAt(redisKey, Date.from(timeSale.timeSaleEndTime()
                .atZone(ZoneId.systemDefault()).toInstant()));
        });*/
    }

    // 타임세일과 상품 할인가격 비교
    private Integer getMaxDiscount(String productId, Integer defaultDiscount) {
        ZSetOperations<String, Object> ops = redisTemplate.opsForZSet();
        Long currentEpoch = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
        Set<Object> timeSaleDiscounts = ops.rangeByScore(TIME_SALE_CACHE_NAME + productId, 0, currentEpoch);

        if (timeSaleDiscounts == null || timeSaleDiscounts.isEmpty()) {
            return defaultDiscount; // 타임세일이 없으면 기본 할인을 반환
        }

        return timeSaleDiscounts.stream()
            .mapToInt(discount -> (Integer) discount)
            .max()
            .orElse(defaultDiscount);
    }

}
