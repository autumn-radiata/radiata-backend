package radiata.service.coupon.core.implementation;

import radiata.common.annotation.Implementation;
import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.coupon.core.domain.model.constant.CouponSaleType;
import radiata.service.coupon.core.domain.model.constant.CouponType;
import radiata.service.coupon.core.implementation.interfaces.CouponCreateRequestDtoValidator;

@Implementation
public class CouponCreateRequestDtoValidatorImpl implements CouponCreateRequestDtoValidator {

    @Override
    public void validate(CouponCreateRequestDto requestDto) {
        if (requestDto.couponType().equals(CouponType.FIRST_COME_FIRST_SERVED.toString())) {
            if (requestDto.totalQuantity() == null) {
                throw new BusinessException(ExceptionMessage.COUPON_INVALID_INPUT_FIRST_COME_FIRST_SERVED);
            }
        }

        if (requestDto.couponType().equals(CouponType.UNLIMITED.toString())) {
            if (requestDto.totalQuantity() != null) {
                throw new BusinessException(ExceptionMessage.COUPON_INVALID_INPUT_UNLIMITED);
            }
        }

        if (requestDto.couponSaleType().equals(CouponSaleType.RATE.toString())) {
            if (requestDto.discountRate() == null) {
                throw new BusinessException(ExceptionMessage.COUPON_INVALID_INPUT_RATE);
            }
            if (requestDto.discountAmount() != null) {
                throw new BusinessException(ExceptionMessage.COUPON_INVALID_INPUT_RATE_DONT_WRITE_DISCOUNT_AMOUNT);
            }
        }

        if (requestDto.couponSaleType().equals(CouponSaleType.AMOUNT.toString())) {
            if (requestDto.discountAmount() == null) {
                throw new BusinessException(ExceptionMessage.COUPON_INVALID_INPUT_AMOUNT);
            }
            if (requestDto.discountRate() != null) {
                throw new BusinessException(ExceptionMessage.COUPON_INVALID_INPUT_AMOUNT_DONT_WRITE_DISCOUNT_RATE);
            }
        }
    }
}
