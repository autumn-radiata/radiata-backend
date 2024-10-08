package radiata.common.domain.coupon.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CouponResponseDto(

    String couponId, // 쿠폰 아이디

    String title, // 쿠폰 이름

    String couponType, // 쿠폰 타입 (FIRST_COME_FIRST_SERVED, UNLIMITED)

    String couponSaleType, // 쿠폰 세일 타입 (RATE, AMOUNT)

    @JsonInclude(NON_NULL) // 값이 없을 경우 Json 에서 제외
    Integer totalQuantity, // 쿠폰 최대 발급 수량

    Integer issuedQuantity, // 현재 쿠폰 발급량

    @JsonInclude(NON_NULL) // 값이 없을 경우 Json 에서 제외
    Integer discountAmount, // 할인 금액

    @JsonInclude(NON_NULL) // 값이 없을 경우 Json 에서 제외
    Integer discountRate, // 할인율

    @JsonInclude(NON_NULL)
    Integer minAvailableAmount, // 최소 사용 금액

    @JsonInclude(NON_NULL)
    Integer manAvailableAmount, // 최대 사용 금액

    LocalDateTime issueStartDate, // 발급 시작일

    LocalDateTime issueEndDate // 발급 종료일
) {


    public static CouponResponseDto of(String couponId, String title, String couponType, String couponSaleType,
        Integer totalQuantity, Integer issuedQuantity, Integer discountAmount, Integer discountRate,
        Integer minAvailableAmount, Integer maxAvailableAmount, LocalDateTime issueStartDate, LocalDateTime issueEndDate
    ) {

        return CouponResponseDto.builder()
            .couponId(couponId)
            .title(title)
            .couponType(couponType)
            .couponSaleType(couponSaleType)
            .totalQuantity(totalQuantity)
            .issuedQuantity(issuedQuantity)
            .discountAmount(discountAmount)
            .discountRate(discountRate)
            .minAvailableAmount(minAvailableAmount)
            .manAvailableAmount(maxAvailableAmount)
            .issueStartDate(issueStartDate)
            .issueEndDate(issueEndDate)
            .build();
    }
}
