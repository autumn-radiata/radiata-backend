package radiata.service.coupon.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.model.CouponIssue;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueIdCreator;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueReader;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueSaver;
import radiata.service.coupon.core.implementation.interfaces.CouponReader;
import radiata.service.coupon.core.service.interfaces.coupon_issue.CouponIssueService;
import radiata.service.coupon.core.service.mapper.CouponIssueMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponIssueServiceImplV1 implements CouponIssueService {

    private final CouponReader couponReader;
    private final CouponIssueSaver couponIssueSaver;
    private final CouponIssueReader couponIssueReader;
    private final CouponIssueMapper couponIssueMapper;
    private final CouponIssueIdCreator couponIssueIdCreator;

    @Override
    public void issue(String couponId, String userId) {
        Coupon coupon = couponReader.readCoupon(couponId);
        coupon.issue();
        saveCouponIssue(couponId, userId);
    }

    public CouponIssue saveCouponIssue(String couponId, String userId) {

        checkAlreadyIssuance(couponId, userId);
        Coupon coupon = couponReader.readCoupon(couponId); // 같은 트랙잭션 내에서 가져와서 성능상 별 차이 없을 거 같아서 넣었습니다.
        String couponIssueId = couponIssueIdCreator.create();

        return couponIssueSaver.save(couponIssueMapper.toEntity(coupon, userId, couponIssueId));
    }

    private void checkAlreadyIssuance(String couponId, String userId) {

        CouponIssue couponIssue = couponIssueReader.readFirstCouponIssue(couponId, userId);

        if (couponIssue != null) {
            throw new BusinessException(ExceptionMessage.DUPLICATED_COUPON_ISSUE);
        }
    }
}
