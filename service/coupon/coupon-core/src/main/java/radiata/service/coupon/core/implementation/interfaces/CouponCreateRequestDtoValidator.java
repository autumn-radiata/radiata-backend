package radiata.service.coupon.core.implementation.interfaces;

import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;

public interface CouponCreateRequestDtoValidator {

    void validate(CouponCreateRequestDto requestDto);
}
