package radiata.service.coupon.core.service.mapper;

import org.springframework.stereotype.Component;
import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.request.CouponUpdateRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.common.domain.coupon.constant.CouponSaleType;
import radiata.common.domain.coupon.constant.CouponType;
import radiata.service.coupon.core.domain.model.vo.CouponDiscountRate;

@Component
public class CouponMapperImpl implements CouponMapper {

    @Override
    public Coupon toEntity(CouponCreateRequestDto requestDto, String couponId) {

        return Coupon.of(
            couponId,
            requestDto.title(),
            CouponType.valueOf(requestDto.couponType()),
            CouponSaleType.valueOf(requestDto.couponSaleType()),
            requestDto.totalQuantity(),
            0,
            requestDto.discountAmount(),
            new CouponDiscountRate(requestDto.discountRate()),
            requestDto.minAvailableAmount(),
            requestDto.maxAvailableAmount(),
            requestDto.issueStartDate(),
            requestDto.issueEndDate()
        );
    }

    @Override
    public CouponResponseDto toDto(Coupon coupon) {

        return CouponResponseDto.of(
            coupon.getId(),
            coupon.getTitle(),
            coupon.getCouponType().toString(),
            coupon.getCouponSaleType().toString(),
            coupon.getTotalQuantity(),
            coupon.getIssuedQuantity(),
            coupon.getDiscountAmount(),
            coupon.getDiscountRate() != null ? coupon.getDiscountRate().getValue() : null,
            coupon.getMinAvailableAmount(),
            coupon.getMaxAvailableAmount(),
            coupon.getIssueStartDate(),
            coupon.getIssueEndDate()
        );
    }

    @Override
    public void update(Coupon coupon, CouponUpdateRequestDto requestDto) {

        coupon.update(
            requestDto.title(),
            CouponType.valueOf(requestDto.couponType()),
            CouponSaleType.valueOf(requestDto.couponSaleType()),
            requestDto.totalQuantity(),
            requestDto.discountAmount(),
            new CouponDiscountRate(requestDto.discountRate()),
            requestDto.minAvailableAmount(),
            requestDto.maxAvailableAmount(),
            requestDto.issueStartDate(),
            requestDto.issueEndDate()
        );
    }
}
