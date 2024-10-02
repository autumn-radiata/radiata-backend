package radiata.service.coupon.core.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import radiata.service.coupon.core.domain.model.CouponIssue;
import radiata.service.coupon.core.domain.repository.CouponIssueRepository;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueSaver;

@Component
@RequiredArgsConstructor
public class CouponIssueSaverImpl implements CouponIssueSaver {

    private final CouponIssueRepository couponIssueRepository;

    @Override
    public CouponIssue save(CouponIssue couponIssue) {

        return couponIssueRepository.save(couponIssue);
    }
}
