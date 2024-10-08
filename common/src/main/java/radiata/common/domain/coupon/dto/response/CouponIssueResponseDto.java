package radiata.common.domain.coupon.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CouponIssueResponseDto(
    String couponIssueId,
    String couponId,
    String userId,
    String couponStatus,
    LocalDateTime issuedAt,
    @JsonInclude(NON_NULL)
    LocalDateTime usedAt,
    LocalDateTime expiredAt
) {

    public static CouponIssueResponseDto of(
        String couponIssueId,
        String couponId,
        String userId,
        String couponStatus,
        LocalDateTime issuedAt,
        LocalDateTime usedAt,
        LocalDateTime expiredAt
    ) {

        return CouponIssueResponseDto.builder()
            .couponIssueId(couponIssueId)
            .couponId(couponId)
            .userId(userId)
            .couponStatus(couponStatus)
            .issuedAt(issuedAt)
            .usedAt(usedAt)
            .expiredAt(expiredAt)
            .build();
    }
}
