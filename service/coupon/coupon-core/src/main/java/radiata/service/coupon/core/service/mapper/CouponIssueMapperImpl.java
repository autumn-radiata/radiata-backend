package radiata.service.coupon.core.service.mapper;

import org.springframework.stereotype.Component;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.model.CouponIssue;

@Component
public class CouponIssueMapperImpl implements CouponIssueMapper {

    @Override
    public CouponIssue toEntity(Coupon coupon, String userId, String couponIssueId) {

        return CouponIssue.from(coupon, userId, couponIssueId);
    }
}
