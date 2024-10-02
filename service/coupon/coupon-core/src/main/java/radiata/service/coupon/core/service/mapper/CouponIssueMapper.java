package radiata.service.coupon.core.service.mapper;

import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.model.CouponIssue;

public interface CouponIssueMapper {

    CouponIssue toEntity(Coupon coupon, String userId, String couponIssueId);

}
