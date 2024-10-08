package radiata.service.coupon.core.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.coupon.core.domain.model.constant.CouponStatus;

class CouponIssueTest {

    @Nested
    @DisplayName("쿠폰 사용 테스트")
    class UseCouponIssue {

        @Test
        @DisplayName("쿠폰 사용에 문제가 없을 경우 사용할 수 있다.")
        void use_1() {
            // Given
            String userId = "userId";
            CouponIssue couponIssue = CouponIssue.builder()
                .issuedAt(LocalDateTime.now().minusDays(1))
                .couponStatus(CouponStatus.ISSUED)
                .userId(userId)
                .expiredAt(LocalDateTime.now().plusDays(1))
                .build();
            // When
            couponIssue.use(userId);
            // Then
            assertThat(couponIssue.getCouponStatus()).isEqualTo(CouponStatus.USED);
            assertThat(couponIssue.getUsedAt()).isCloseTo(LocalDateTime.now(), new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS));
        }

        @Test
        @DisplayName("쿠폰 만료 기간이 지난 경우 사용 할 수 없다. - ExpiredAt")
        void use_2() {
            // Given
            String userId = "userId";
            CouponIssue couponIssue = CouponIssue.builder()
                .issuedAt(LocalDateTime.now().minusDays(1))
                .couponStatus(CouponStatus.ISSUED)
                .userId(userId)
                .expiredAt(LocalDateTime.now().minusDays(1))
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(() -> couponIssue.use(userId),
                BusinessException.class);
            assertThat(result).hasMessage(ExceptionMessage.COUPON_CAN_NOT_USE.getMessage());
        }

        @Test
        @DisplayName("쿠폰을 사용한 경우 사용 할 수 없다. - Status")
        void use_3() {
            // Given
            String userId = "userId";
            CouponIssue couponIssue = CouponIssue.builder()
                .issuedAt(LocalDateTime.now().minusDays(1))
                .couponStatus(CouponStatus.USED)
                .userId(userId)
                .expiredAt(LocalDateTime.now().plusDays(1))
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(() -> couponIssue.use(userId),
                BusinessException.class);
            assertThat(result).hasMessage(ExceptionMessage.COUPON_CAN_NOT_USE.getMessage());
        }

        @Test
        @DisplayName("쿠폰을 사용한 경우 사용 할 수 없다. - UsedAt")
        void use_4() {
            // Given
            String userId = "userId";
            CouponIssue couponIssue = CouponIssue.builder()
                .issuedAt(LocalDateTime.now().minusDays(1))
                .couponStatus(CouponStatus.ISSUED)
                .usedAt(LocalDateTime.now().minusHours(1))
                .userId(userId)
                .expiredAt(LocalDateTime.now().plusDays(1))
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(() -> couponIssue.use(userId),
                BusinessException.class);
            assertThat(result).hasMessage(ExceptionMessage.COUPON_CAN_NOT_USE.getMessage());
        }

        @Test
        @DisplayName("쿠폰이 만료 된 경우 사용 할 수 없다. - Status")
        void use_5() {
            // Given
            String userId = "userId";
            CouponIssue couponIssue = CouponIssue.builder()
                .issuedAt(LocalDateTime.now().minusDays(1))
                .couponStatus(CouponStatus.EXPIRED)
                .userId(userId)
                .expiredAt(LocalDateTime.now().plusDays(1))
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(() -> couponIssue.use(userId),
                BusinessException.class);
            assertThat(result).hasMessage(ExceptionMessage.COUPON_CAN_NOT_USE.getMessage());
        }
    }

}