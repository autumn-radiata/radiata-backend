package radiata.service.coupon.core.implementation;

import lombok.RequiredArgsConstructor;
import radiata.common.annotation.Implementation;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.repository.RedisRepository;
import radiata.service.coupon.core.implementation.interfaces.CouponIssueValidator;
import radiata.service.coupon.core.util.RedisKeyUtil;

@Implementation
@RequiredArgsConstructor
public class CouponIssueValidatorImpl implements CouponIssueValidator {

    private final RedisRepository redisRepository;

    @Override
    public void validate(Coupon coupon, String userId) {

        if (!coupon.availableIssueDate()) {
            throw new BusinessException(ExceptionMessage.COUPON_ISSUE_PERIOD);
        }

        if (!availableTotalIssueQuantity(coupon)) {
            throw new BusinessException(ExceptionMessage.COUPON_ISSUE_QUANTITY_LIMITED);
        }

        if (!availableUserIssueQuantity(coupon, userId)) {
            throw new BusinessException(ExceptionMessage.DUPLICATED_COUPON_ISSUE);
        }

    }

    private boolean availableTotalIssueQuantity(Coupon coupon) {

        if (coupon.getCouponType().isUnlimited()) {
            return true;
        }

        String key = RedisKeyUtil.getIssueRequestKey(coupon.getId());

        return coupon.getTotalQuantity() > redisRepository.sCard(key);
    }

    private boolean availableUserIssueQuantity(Coupon coupon, String userId) {

        String key = RedisKeyUtil.getIssueRequestKey(coupon.getId());

        return !redisRepository.sIsMember(key, userId);
    }
}
