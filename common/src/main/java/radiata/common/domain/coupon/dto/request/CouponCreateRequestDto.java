package radiata.common.domain.coupon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Range;

public record CouponCreateRequestDto(

    @NotBlank(message = "쿠폰명을 입력해주세요.")
    @Size(min = 1, max = 100, message = "쿠폰명은 1 ~ 100 글자까지 입력할 수 있습니다.")
    String title, // 쿠폰명

    @NotBlank(message = "쿠폰 타입을 입력해주세요.")
    String couponType, // 쿠폰 타입 (FIRST_COME_FIRST_SERVED, UNLIMITED)

    @NotBlank(message = "쿠폰 세일 타입을 입력해주세요.")
    String couponSaleType, // 쿠폰 세일 타입 (RATE, AMOUNT)

    @Positive(message = "쿠폰 발급 수량은 음수가 될 수 없습니다.")
    Integer totalQuantity, // 쿠폰 최대 발급 수량

    @Positive(message = "할인 금액은 음수가 될 수 없습니다.")
    Integer discountAmount, // 할인 금액

    @Range(min = 1, max = 100, message = "할인율은 최대 1% ~ 100% 까지 입력할 수 있습니다.")
    @Positive(message = "할인율은 음수가 될 수 없습니다.")
    Integer discountRate, // 할인율

    @Positive(message = "최소 사용 금액은 음수가 될 수 없습니다.")
    Integer minAvailableAmount, // 최소 사용 금액

    @NotNull(message = "발급 시작일을 입력해주세요.")
    LocalDateTime issueStartDate, // 발급 시작일

    @NotNull(message = "발급 종료일을 입력해주세요.")
    LocalDateTime issueEndDate // 발급 종료일
) {

}
