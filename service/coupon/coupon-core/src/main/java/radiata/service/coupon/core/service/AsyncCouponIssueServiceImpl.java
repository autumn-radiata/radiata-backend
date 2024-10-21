package radiata.service.coupon.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import radiata.common.domain.coupon.dto.CouponIssueRequestDto;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueSaver;
import radiata.service.coupon.core.implementation.interfaces.CouponReader;
import radiata.service.coupon.core.service.interfaces.coupon_issue.AsyncCouponIssueService;
import radiata.service.coupon.core.util.KafkaKeyUtil;

@Service
@RequiredArgsConstructor
public class AsyncCouponIssueServiceImpl implements AsyncCouponIssueService {

    private final CouponReader couponReader;
    private final CouponIssueSaver couponIssueSaver;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void issue(String couponId, String userId) {

        Coupon coupon = couponReader.readCoupon(couponId);
        coupon.checkIssuableCoupon();
        saveCouponIssue(couponId, userId, coupon.getTotalQuantity());
    }

    @Override
    public void saveCouponIssue(String couponId, String userId, Integer totalQuantity) {
        if (totalQuantity == null) {
            couponIssueSaver.saveToRedis(couponId, userId, Integer.MAX_VALUE);
        }
        couponIssueSaver.saveToRedis(couponId, userId, totalQuantity);
        requestCouponIssueToQueue(couponId, userId);
    }

    private void requestCouponIssueToQueue(String couponId, String userId) {

        CouponIssueRequestDto couponIssueRequestDto = new CouponIssueRequestDto(couponId, userId);
        kafkaTemplate.send(KafkaKeyUtil.couponIssueRequestKey, couponIssueRequestDto);
    }
}
