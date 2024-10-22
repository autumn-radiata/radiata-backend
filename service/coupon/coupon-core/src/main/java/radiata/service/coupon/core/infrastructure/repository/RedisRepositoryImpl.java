package radiata.service.coupon.core.infrastructure.repository;

import static radiata.service.coupon.core.util.RedisKeyUtil.getIssueRequestKey;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;
import radiata.service.coupon.core.domain.repository.RedisRepository;
import radiata.service.coupon.core.util.CouponIssueRequestCode;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisScript<String> issueScript = issueRequestScript();

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

    @Override
    public void issueRequest(String couponId, String userId, Integer totalIssueQuantity) {

        String key = getIssueRequestKey(couponId);

        String code = redisTemplate.execute(
                issueScript,
                List.of(key),
                userId,
                String.valueOf(totalIssueQuantity)
        );

        CouponIssueRequestCode.checkRequestResult(CouponIssueRequestCode.fromCode(code));
    }

    private RedisScript<String> issueRequestScript() {
        String script = """
                if redis.call('SISMEMBER', KEYS[1], ARGV[1]) == 1 then
                    return '2'
                end
                
                if tonumber(ARGV[2]) > redis.call('SCARD', KEYS[1]) then
                    redis.call('SADD', KEYS[1], ARGV[1])
                    return '1'
                end
                
                return '3'
                """;

        // 1. 유저 중복 확인
        // 2. 쿠폰 발급 수량 확인

        return RedisScript.of(script, String.class);
    }
}
