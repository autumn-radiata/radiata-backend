package radiata.service.coupon.core.implementation;

import lombok.RequiredArgsConstructor;
import radiata.common.annotation.Implementation;
import radiata.service.coupon.core.domain.model.CouponIssue;
import radiata.service.coupon.core.domain.repository.CouponIssueRepository;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueDeleter;

@Implementation
@RequiredArgsConstructor
public class CouponIssueDeleterImpl implements CouponIssueDeleter {

    private final CouponIssueRepository couponIssueRepository;

    @Override
    public void delete(CouponIssue couponIssue) {

        couponIssueRepository.delete(couponIssue);
    }
}
