package radiata.common.domain.order.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CouponInfoDto(String saleType, Integer discountValue) {

    public static CouponInfoDto of(String saleType, Integer discountValue) {

        return CouponInfoDto.builder()
            .saleType(saleType)
            .discountValue(discountValue)
            .build();
    }
}
