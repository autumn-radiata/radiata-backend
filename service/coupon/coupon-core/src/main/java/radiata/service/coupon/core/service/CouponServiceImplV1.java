package radiata.service.coupon.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.coupon.dto.condition.CouponSearchCondition;
import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.request.CouponUpdateRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.model.constant.CouponSaleType;
import radiata.service.coupon.core.domain.model.constant.CouponType;
import radiata.service.coupon.core.implementation.interfaces.CouponDeleter;
import radiata.service.coupon.core.implementation.interfaces.CouponIdCreator;
import radiata.service.coupon.core.implementation.interfaces.CouponReader;
import radiata.service.coupon.core.implementation.interfaces.CouponSaver;
import radiata.service.coupon.core.service.interfaces.CouponService;
import radiata.service.coupon.core.service.mapper.CouponMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponServiceImplV1 implements CouponService {

    private final CouponMapper couponMapper;
    private final CouponIdCreator couponIdCreator;
    private final CouponSaver couponSaver;
    private final CouponReader couponReader;
    private final CouponDeleter couponDeleter;

    @Override
    public CouponResponseDto createCoupon(CouponCreateRequestDto requestDto) {

        validateCouponCreateRequestDto(requestDto);

        String couponId = couponIdCreator.create();

        Coupon savedCoupon = couponSaver.save(couponMapper.toEntity(requestDto, couponId));

        return couponMapper.toDto(savedCoupon);
    }

    private void validateCouponCreateRequestDto(CouponCreateRequestDto requestDto) {

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

    @Override
    @Transactional(readOnly = true)
    public CouponResponseDto getCoupon(String couponId) {

        return couponMapper.toDto(couponReader.readCoupon(couponId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CouponResponseDto> getCoupons(CouponSearchCondition condition, Pageable pageable) {

        return couponReader.readCouponsByCondition(condition, pageable).map(couponMapper::toDto);
    }

    @Override
    public void deleteCoupon(String couponId) {

        Coupon coupon = couponReader.readCoupon(couponId);

        couponDeleter.deleteCoupon(coupon);
    }

    @Override
    public CouponResponseDto updateCoupon(String couponId, CouponUpdateRequestDto requestDto) {

        Coupon coupon = couponReader.readCoupon(couponId);
        couponMapper.update(coupon, requestDto);

        return couponMapper.toDto(coupon);
    }
}
