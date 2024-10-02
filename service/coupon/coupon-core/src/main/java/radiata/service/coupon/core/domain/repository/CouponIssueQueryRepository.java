package radiata.service.coupon.core.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;
import radiata.service.coupon.core.domain.model.CouponIssue;

public interface CouponIssueQueryRepository {

    CouponIssue findFirstCouponIssue(String couponId, String userId);

    Page<CouponIssue> findCouponIssuesByUserId(String userId, Pageable pageable);
}
