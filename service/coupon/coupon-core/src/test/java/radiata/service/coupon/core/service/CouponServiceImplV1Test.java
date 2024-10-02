package radiata.service.coupon.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.domain.coupon.dto.request.CouponCreateRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.coupon.core.CouponCoreConfiguration;
import radiata.service.coupon.core.domain.model.constant.CouponSaleType;
import radiata.service.coupon.core.domain.model.constant.CouponType;
import radiata.service.coupon.core.implementation.interfaces.CouponIdCreator;
import radiata.service.coupon.core.implementation.interfaces.CouponSaver;
import radiata.service.coupon.core.service.interfaces.CouponService;
import radiata.service.coupon.core.service.mapper.CouponMapper;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(classes = CouponCoreConfiguration.class)
class CouponServiceImplV1Test {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponSaver couponSaver;

    @Autowired
    CouponMapper couponMapper;

    @Autowired
    CouponIdCreator couponIdCreator;


    @Nested
    @DisplayName("쿠폰 저장 테스트")
    class CreateCoupon {

        @Test
        @DisplayName("CouponSaleType 이 Amount 면 discountAmount 만 설정할 수 있다.")
        void createCoupon_1() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            String title = "제목";
            Integer totalQuantity = 1000;
            Integer discountAmount = 1000;
            CouponCreateRequestDto requestDto = CouponCreateRequestDto.builder()
                .title("제목")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED.toString())
                .couponSaleType(CouponSaleType.AMOUNT.toString())
                .totalQuantity(totalQuantity)
                .discountAmount(discountAmount)
                .discountRate(null)
                .issueStartDate(now.plusDays(1))
                .issueEndDate(now.plusDays(2))
                .build();
            // When
            CouponResponseDto result = couponService.createCoupon(requestDto);
            // Then
            assertThat(result).isNotNull();
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.couponType()).isEqualTo(CouponType.FIRST_COME_FIRST_SERVED.toString());
            assertThat(result.couponSaleType()).isEqualTo(CouponSaleType.AMOUNT.toString());
            assertThat(result.totalQuantity()).isEqualTo(totalQuantity);
            assertThat(result.issuedQuantity()).isEqualTo(0);
            assertThat(result.discountAmount()).isEqualTo(discountAmount);
            assertThat(result.discountRate()).isEqualTo(null);
            assertThat(result.issueStartDate()).isEqualTo(now.plusDays(1));
            assertThat(result.issueEndDate()).isEqualTo(now.plusDays(2));
        }

        @Test
        @DisplayName("CouponSaleType 이 Rate 면 discountRate 만 설정할 수 있다.")
        void createCoupon_2() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            String title = "제목";
            Integer totalQuantity = 1000;
            Integer discountRate = 50;
            CouponCreateRequestDto requestDto = CouponCreateRequestDto.builder()
                .title("제목")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED.toString())
                .couponSaleType(CouponSaleType.RATE.toString())
                .totalQuantity(totalQuantity)
                .discountAmount(null)
                .discountRate(discountRate)
                .issueStartDate(now.plusDays(1))
                .issueEndDate(now.plusDays(2))
                .build();
            // When
            CouponResponseDto result = couponService.createCoupon(requestDto);
            // Then
            assertThat(result).isNotNull();
            assertThat(result.title()).isEqualTo(title);
            assertThat(result.couponType()).isEqualTo(CouponType.FIRST_COME_FIRST_SERVED.toString());
            assertThat(result.couponSaleType()).isEqualTo(CouponSaleType.RATE.toString());
            assertThat(result.totalQuantity()).isEqualTo(totalQuantity);
            assertThat(result.issuedQuantity()).isEqualTo(0);
            assertThat(result.discountAmount()).isEqualTo(null);
            assertThat(result.discountRate()).isEqualTo(discountRate);
            assertThat(result.issueStartDate()).isEqualTo(now.plusDays(1));
            assertThat(result.issueEndDate()).isEqualTo(now.plusDays(2));
        }

        @Test
        @DisplayName("CouponSaleType 이 Rate 면 discountAmount 를 입력할 수 없다.")
        void createCoupon_3() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            String title = "제목";
            Integer totalQuantity = 1000;
            Integer discountRate = 50;
            Integer discountAmount = 50;
            CouponCreateRequestDto requestDto = CouponCreateRequestDto.builder()
                .title(title)
                .couponType(CouponType.FIRST_COME_FIRST_SERVED.toString())
                .couponSaleType(CouponSaleType.RATE.toString())
                .totalQuantity(totalQuantity)
                .discountAmount(discountAmount)
                .discountRate(discountRate)
                .issueStartDate(now.plusDays(1))
                .issueEndDate(now.plusDays(2))
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(() -> couponService.createCoupon(requestDto),
                BusinessException.class);

            assertThat(result).hasMessage(
                ExceptionMessage.COUPON_INVALID_INPUT_RATE_DONT_WRITE_DISCOUNT_AMOUNT.getMessage());
        }

        @Test
        @DisplayName("CouponSaleType 이 Amount 면 discountRate 를 입력할 수 없다.")
        void createCoupon_4() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            String title = "제목";
            Integer totalQuantity = 1000;
            Integer discountRate = 50;
            Integer discountAmount = 50;
            CouponCreateRequestDto requestDto = CouponCreateRequestDto.builder()
                .title(title)
                .couponType(CouponType.FIRST_COME_FIRST_SERVED.toString())
                .couponSaleType(CouponSaleType.AMOUNT.toString())
                .totalQuantity(totalQuantity)
                .discountAmount(discountAmount)
                .discountRate(discountRate)
                .issueStartDate(now.plusDays(1))
                .issueEndDate(now.plusDays(2))
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(() -> couponService.createCoupon(requestDto),
                BusinessException.class);

            assertThat(result).hasMessage(
                ExceptionMessage.COUPON_INVALID_INPUT_AMOUNT_DONT_WRITE_DISCOUNT_RATE.getMessage());
        }

        @Test
        @DisplayName("CouponSaleType 이 Amount 면 discountAmount 를 입력해야 한다.")
        void createCoupon_5() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            String title = "제목";
            Integer totalQuantity = 1000;
            CouponCreateRequestDto requestDto = CouponCreateRequestDto.builder()
                .title(title)
                .couponType(CouponType.FIRST_COME_FIRST_SERVED.toString())
                .couponSaleType(CouponSaleType.AMOUNT.toString())
                .totalQuantity(totalQuantity)
                .discountAmount(null)
                .issueStartDate(now.plusDays(1))
                .issueEndDate(now.plusDays(2))
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(() -> couponService.createCoupon(requestDto),
                BusinessException.class);

            assertThat(result).hasMessage(
                ExceptionMessage.COUPON_INVALID_INPUT_AMOUNT.getMessage());
        }

        @Test
        @DisplayName("CouponSaleType 이 Rate 면 discountRate 를 입력해야 한다.")
        void createCoupon_6() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            String title = "제목";
            Integer totalQuantity = 1000;
            CouponCreateRequestDto requestDto = CouponCreateRequestDto.builder()
                .title(title)
                .couponType(CouponType.FIRST_COME_FIRST_SERVED.toString())
                .couponSaleType(CouponSaleType.RATE.toString())
                .totalQuantity(totalQuantity)
                .discountRate(null)
                .issueStartDate(now.plusDays(1))
                .issueEndDate(now.plusDays(2))
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(() -> couponService.createCoupon(requestDto),
                BusinessException.class);

            assertThat(result).hasMessage(
                ExceptionMessage.COUPON_INVALID_INPUT_RATE.getMessage());
        }

        @Test
        @DisplayName("CouponType 이 선착순이면 발급량을 입력해야 한다.")
        void createCoupon_7() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            String title = "제목";
            Integer discountRate = 100;
            CouponCreateRequestDto requestDto = CouponCreateRequestDto.builder()
                .title(title)
                .couponType(CouponType.FIRST_COME_FIRST_SERVED.toString())
                .couponSaleType(CouponSaleType.RATE.toString())
                .totalQuantity(null)
                .discountRate(discountRate)
                .issueStartDate(now.plusDays(1))
                .issueEndDate(now.plusDays(2))
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(() -> couponService.createCoupon(requestDto),
                BusinessException.class);

            assertThat(result).hasMessage(
                ExceptionMessage.COUPON_INVALID_INPUT_FIRST_COME_FIRST_SERVED.getMessage());
        }

        @Test
        @DisplayName("CouponType 이 무제한이면 발급량을 입력할 수 없다.")
        void createCoupon_8() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            String title = "제목";
            Integer discountRate = 100;
            Integer totalQuantity = 10;
            CouponCreateRequestDto requestDto = CouponCreateRequestDto.builder()
                .title(title)
                .couponType(CouponType.UNLIMITED.toString())
                .couponSaleType(CouponSaleType.RATE.toString())
                .totalQuantity(totalQuantity)
                .discountRate(discountRate)
                .issueStartDate(now.plusDays(1))
                .issueEndDate(now.plusDays(2))
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(() -> couponService.createCoupon(requestDto),
                BusinessException.class);

            assertThat(result).hasMessage(
                ExceptionMessage.COUPON_INVALID_INPUT_UNLIMITED.getMessage());
        }
    }

}