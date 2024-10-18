package radiata.service.coupon.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.model.CouponIssue;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueDeleter;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueReader;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueSaver;
import radiata.service.coupon.core.implementation.interfaces.CouponReader;
import radiata.service.coupon.core.service.interfaces.coupon_issue.CouponIssueService;
import radiata.service.coupon.core.service.mapper.CouponIssueMapper;

@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class CouponIssueServiceImplV2 implements CouponIssueService {

    private final CouponReader couponReader;
    private final CouponIssueReader couponIssueReader;
    private final CouponIssueMapper couponIssueMapper;
    private final CouponIssueDeleter couponIssueDeleter;
    private final CouponIssueSaver couponIssueSaver;


    @Override
    public void issue(String couponId, String userId) {

        Coupon coupon = couponReader.readCoupon(couponId);
        coupon.checkIssuableCoupon();
        saveCouponIssue(couponId, userId, coupon.getTotalQuantity());
    }

    @Override
    public void saveCouponIssue(String couponId, String userId, Integer totalQuantity) {
        if (totalQuantity == null) {
            couponIssueSaver.saveToRedis(couponId, userId, Integer.MAX_VALUE);
        }
        couponIssueSaver.saveToRedis(couponId, userId, totalQuantity);
        // TODO 큐에 적재
    }

    private void requestCouponIssueToQueue(String couponId, String userId) {

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

        return couponIssueReader.readCouponIssuesByUserId(userId, pageable)
                .map(couponIssueMapper::toDto);
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
