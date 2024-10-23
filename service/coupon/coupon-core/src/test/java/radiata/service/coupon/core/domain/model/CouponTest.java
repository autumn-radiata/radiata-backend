package radiata.service.coupon.core.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.common.domain.coupon.constant.CouponType;

class CouponTest {

    @Nested
    @DisplayName("발급 수량 테스트")
    class 발급_수량_테스트 {

        @Test
        @DisplayName("발급 수량이 남아있다면 True를 반환한다.")
        void availableIssueQuantity_1() {
            // Given
            Coupon coupon = Coupon.builder()
                .issuedQuantity(0)
                .totalQuantity(100)
                .build();
            // When
            boolean result = coupon.availableIssuedQuantity();
            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("발급 수량이 남아있다면 True를 반환한다.")
        void availableIssueQuantity_2() {
            // Given
            Coupon coupon = Coupon.builder()
                .issuedQuantity(99)
                .totalQuantity(100)
                .build();
            // When
            boolean result = coupon.availableIssuedQuantity();
            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("발급 수량이 소진됐다면 False를 반환한다. - 1")
        void availableIssueQuantity_3() {
            // Given
            Coupon coupon = Coupon.builder()
                .issuedQuantity(100)
                .totalQuantity(100)
                .build();
            // When
            boolean result = coupon.availableIssuedQuantity();
            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("발급 수량이 소진됐다면 False를 반환한다. - 2")
        void availableIssueQuantity_4() {
            // Given
            Coupon coupon = Coupon.builder()
                .issuedQuantity(101)
                .totalQuantity(100)
                .build();
            // When
            boolean result = coupon.availableIssuedQuantity();
            // Then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("쿠폰 이슈 테스트")
    class 쿠폰_이슈_테스트 {

        @Test
        @DisplayName("쿠폰 타입 - 무제한 && 쿠폰 발급 가능 일자 = 현재 쿠폰 발급 수량을 증가")
        void issueCouponType_1() {
            // Given
            Coupon coupon = Coupon.builder()
                .couponType(CouponType.UNLIMITED)
                .issueStartDate(LocalDateTime.now().minusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(1))
                .issuedQuantity(0)
                .build();
            // When
            coupon.issue();
            // Then
            assertThat(coupon.getIssuedQuantity()).isEqualTo(1);
        }

        @Test
        @DisplayName("쿠폰 타입 - 무제한 && 쿠폰 발급 불가능 일자 = 예외")
        void issueCouponType_2() {
            // Given
            Coupon coupon = Coupon.builder()
                .couponType(CouponType.UNLIMITED)
                .issueStartDate(LocalDateTime.now().plusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(2))
                .issuedQuantity(0)
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(() -> coupon.issue(),
                BusinessException.class);

            assertThat(result).hasMessage(ExceptionMessage.COUPON_ISSUE_PERIOD.getMessage());
            assertThat(result.getCode()).isEqualTo(ExceptionMessage.COUPON_ISSUE_PERIOD.getCode());
            assertThat(result.getHttpStatus()).isEqualTo(ExceptionMessage.COUPON_ISSUE_PERIOD.getHttpStatus());
        }

        @Test
        @DisplayName("쿠폰 타입 - 선착순 && 쿠폰 발급 가능 일자 = 현재 쿠폰 발급 수량을 증가")
        void issueCouponType_3() {
            // Given
            Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .issueStartDate(LocalDateTime.now().minusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(1))
                .issuedQuantity(0)
                .totalQuantity(100)
                .build();
            // When
            coupon.issue();
            // Then
            assertThat(coupon.getIssuedQuantity()).isEqualTo(1);
        }

        @Test
        @DisplayName("쿠폰 타입 - 선착순 && 쿠폰 발급 불가능 일자 = 예외")
        void issueCouponType_4() {
            // Given
            Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .issueStartDate(LocalDateTime.now().plusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(2))
                .issuedQuantity(0)
                .totalQuantity(100)
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(() -> coupon.issue(),
                BusinessException.class);

            assertThat(result).hasMessage(ExceptionMessage.COUPON_ISSUE_PERIOD.getMessage());
            assertThat(result.getCode()).isEqualTo(ExceptionMessage.COUPON_ISSUE_PERIOD.getCode());
            assertThat(result.getHttpStatus()).isEqualTo(ExceptionMessage.COUPON_ISSUE_PERIOD.getHttpStatus());
        }

        @Test
        @DisplayName("쿠폰 타입 - 선착순 && 쿠폰 발급 불가능 일자 && 쿠폰 수량 소진 = 예외")
        void issueCouponType_5() {
            // Given
            Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .issueStartDate(LocalDateTime.now().minusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(1))
                .issuedQuantity(100)
                .totalQuantity(100)
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(() -> coupon.issue(),
                BusinessException.class);

            assertThat(result).hasMessage(ExceptionMessage.COUPON_ISSUE_QUANTITY_LIMITED.getMessage());
            assertThat(result.getCode()).isEqualTo(ExceptionMessage.COUPON_ISSUE_QUANTITY_LIMITED.getCode());
            assertThat(result.getHttpStatus()).isEqualTo(
                ExceptionMessage.COUPON_ISSUE_QUANTITY_LIMITED.getHttpStatus());
        }
    }

    @Nested
    @DisplayName("쿠폰 발급 시간 테스트")
    class 쿠폰_발급_시간_테스트 {

        @Test
        @DisplayName("발급 시간이 맞으면 True를 반환한다.")
        void availableIssueDate_1() {
            // Given
            Coupon coupon = Coupon.builder()
                .issueStartDate(LocalDateTime.now().minusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(1))
                .build();
            // When
            boolean result = coupon.availableIssueDate();
            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("발급 시간이 지났으면 Fasle를 반환한다.")
        void availableIssueDate_2() {
            // Given
            Coupon coupon = Coupon.builder()
                .issueStartDate(LocalDateTime.now().minusDays(2))
                .issueEndDate(LocalDateTime.now().minusDays(1))
                .build();
            // When
            boolean result = coupon.availableIssueDate();
            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("발급 시간이 안 됐으면 False를 반환한다.")
        void availableIssueDate_3() {
            // Given
            Coupon coupon = Coupon.builder()
                .issueStartDate(LocalDateTime.now().plusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(2))
                .build();
            // When
            boolean result = coupon.availableIssueDate();
            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("발급 시간이 현재 시각과 동일하면 True를 반환한다.")
        void availableIssueDate_4() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            Coupon coupon = Coupon.builder()
                .issueStartDate(now)
                .issueEndDate(LocalDateTime.now().plusDays(1))
                .build();
            // When
            boolean result = coupon.availableIssueDate();
            // Then
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("선착순이야? 테스트")
    class 선착순이야_테스트 {

        @Test
        @DisplayName("선착순이면 True를 반환한다.")
        void isFirstComeFirstServed_1() {
            // Given
            Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .build();
            // When
            boolean result = coupon.isFirstComeFirstServed();
            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("선착순이 아니면 False를 반환한다.")
        void isFirstComeFirstServed_2() {
            // Given
            Coupon coupon = Coupon.builder()
                .couponType(CouponType.UNLIMITED)
                .build();
            // When
            boolean result = coupon.isFirstComeFirstServed();
            // Then
            assertThat(result).isFalse();
        }
    }

}