package radiata.service.coupon.core.service.mapper;

import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.model.CouponIssue;

public interface CouponIssueMapper {

    CouponIssue toEntity(Coupon coupon, String userId, String couponIssueId);

    CouponIssueResponseDto toDto(CouponIssue couponIssue);

}
