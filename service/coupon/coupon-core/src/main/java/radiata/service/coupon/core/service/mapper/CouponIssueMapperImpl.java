package radiata.service.coupon.core.service.mapper;

import org.springframework.stereotype.Component;
import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.model.CouponIssue;

@Component
public class CouponIssueMapperImpl implements CouponIssueMapper {

    @Override
    public CouponIssue toEntity(Coupon coupon, String userId, String couponIssueId) {

        return CouponIssue.from(coupon, userId, couponIssueId);
    }

    @Override
    public CouponIssueResponseDto toDto(CouponIssue couponIssue) {

        return CouponIssueResponseDto.of(
            couponIssue.getId(),
            couponIssue.getCoupon().getId(),
            couponIssue.getUserId(),
            couponIssue.getCouponStatus().toString(),
            couponIssue.getIssuedAt(),
            couponIssue.getUsedAt(),
            couponIssue.getExpiredAt()
        );
    }
}
