package radiata.service.coupon.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.coupon.dto.CouponIssueRequestDto;
import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.model.CouponIssue;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueDeleter;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueIdCreator;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueReader;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueSaver;
import radiata.service.coupon.core.implementation.interfaces.CouponReader;
import radiata.service.coupon.core.implementation.interfaces.CouponSaver;
import radiata.service.coupon.core.service.interfaces.coupon_issue.CouponIssueService;
import radiata.service.coupon.core.service.mapper.CouponIssueMapper;
import radiata.service.coupon.core.util.KafkaKeyUtil;

@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class CouponIssueServiceImplV2 implements CouponIssueService {

    private final CouponReader couponReader;
    private final CouponSaver couponSaver;
    private final CouponIssueReader couponIssueReader;
    private final CouponIssueMapper couponIssueMapper;
    private final CouponIssueDeleter couponIssueDeleter;
    private final CouponIssueSaver couponIssueSaver;
    private final CouponIssueIdCreator couponIssueIdCreator;


    @Override
    public void issue(String couponId, String userId) {
        Coupon coupon = couponReader.readCoupon(couponId);
        coupon.issue();
        couponSaver.save(coupon);
        saveCouponIssue(couponId, userId, coupon.getTotalQuantity());
    }

    @Override
    public void saveCouponIssue(String couponId, String userId, Integer totalQuantity) {

        checkAlreadyIssuance(couponId, userId);
        Coupon coupon = couponReader.readCoupon(couponId); // 같은 트랙잭션 내에서 가져와서 성능상 별 차이 없을 거 같아서 넣었습니다.
        String couponIssueId = couponIssueIdCreator.create();
        couponIssueSaver.save(couponIssueMapper.toEntity(coupon, userId, couponIssueId));
    }

    private void checkAlreadyIssuance(String couponId, String userId) {

        CouponIssue couponIssue = couponIssueReader.readFirstCouponIssue(couponId, userId);

        if (couponIssue != null) {
            throw new BusinessException(ExceptionMessage.DUPLICATED_COUPON_ISSUE);
        }
    }

    @Override
    public CouponIssueResponseDto useCouponIssue(String couponIssueId, String userId) {

        CouponIssue couponIssue = couponIssueReader.readCouponIssue(couponIssueId);

        couponIssue.use(userId);

        return couponIssueMapper.toDto(couponIssue);
    }

    @Override
    public CouponIssueResponseDto getCouponIssue(String couponIssueId, String userId) {

        CouponIssue couponIssue = couponIssueReader.readCouponIssue(couponIssueId);

        if (!couponIssue.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionMessage.NOT_AUTHORIZED);
        }

        return couponIssueMapper.toDto(couponIssue);
    }

    @Override
    public Page<CouponIssueResponseDto> getCouponIssues(String userId, Pageable pageable) {

        return couponIssueReader.readCouponIssuesByUserId(userId, pageable).map(couponIssueMapper::toDto);
    }

    @Override
    public void deleteCouponIssue(String couponIssueId, String userId) {
        CouponIssue couponIssue = couponIssueReader.readCouponIssue(couponIssueId);

        if (!couponIssue.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionMessage.NOT_AUTHORIZED);
        }

        couponIssueDeleter.delete(couponIssue);
    }
}
