package radiata.service.coupon.core.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import radiata.service.coupon.core.domain.model.CouponIssue;
import radiata.service.coupon.core.domain.repository.CouponIssueRepository;
import radiata.service.coupon.core.domain.repository.RedisRepository;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueSaver;
import radiata.service.coupon.core.util.RedisKeyUtil;

@Component
@RequiredArgsConstructor
public class CouponIssueSaverImpl implements CouponIssueSaver {

    private final CouponIssueRepository couponIssueRepository;
    private final RedisRepository redisRepository;

    @Override
    public CouponIssue save(CouponIssue couponIssue) {

        return couponIssueRepository.save(couponIssue);
    }

    @Override
    public void saveToRedis(String couponId, String userId, Integer totalQuantity) {

        redisRepository.issueRequest(couponId, userId, totalQuantity);
    }
}
