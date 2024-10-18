package radiata.common.domain.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemCreateRequestDto(
    String productId,

    String timeSaleProductId,

    String couponIssuedId,

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    Integer quantity,

    @NotNull(message = "단가는 필수입니다.")
    @Min(value = 1, message = "단가는 최소 1원 이상이어야 합니다.")
    Integer unitPrice) {

}
