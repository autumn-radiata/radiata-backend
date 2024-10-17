package radiata.service.coupon.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.coupon.dto.condition.CouponSearchCondition;
import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.request.CouponUpdateRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.implementation.interfaces.CouponCreateRequestDtoValidator;
import radiata.service.coupon.core.implementation.interfaces.CouponDeleter;
import radiata.service.coupon.core.implementation.interfaces.CouponIdCreator;
import radiata.service.coupon.core.implementation.interfaces.CouponReader;
import radiata.service.coupon.core.implementation.interfaces.CouponSaver;
import radiata.service.coupon.core.service.interfaces.coupon.CouponService;
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
    private final CouponCreateRequestDtoValidator couponCreateRequestDtoValidator;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public CouponResponseDto createCoupon(CouponCreateRequestDto requestDto) {

        couponCreateRequestDtoValidator.validate(requestDto);

        String couponId = couponIdCreator.create();

        Coupon savedCoupon = couponSaver.save(couponMapper.toEntity(requestDto, couponId));

        try {
            redisTemplate.opsForValue().set(savedCoupon.getId(), objectMapper.writeValueAsString(savedCoupon));
        } catch (JsonProcessingException e) {
            throw new BusinessException(ExceptionMessage.SYSTEM_ERROR);
        }

        return couponMapper.toDto(savedCoupon);
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
