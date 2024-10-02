package radiata.service.coupon.core.implementation;

import lombok.RequiredArgsConstructor;
import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.coupon.core.domain.model.CouponIssue;
import radiata.service.coupon.core.domain.repository.CouponIssueQueryRepository;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueReader;

@Implementation
@RequiredArgsConstructor
public class CouponIssueReaderImpl implements CouponIssueReader {

    private final CouponIssueQueryRepository couponIssueQueryRepository;

    @Override
    public CouponIssue readCouponIssue(String couponIssueId) {
        return null;
    }

    @Override
    public CouponIssue readFirstCouponIssue(String couponId, String userId) {

        return couponIssueQueryRepository.findFirstCouponIssue(couponId, userId);
    }
}
