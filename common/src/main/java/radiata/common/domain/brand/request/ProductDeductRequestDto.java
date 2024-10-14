package radiata.common.domain.brand.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductDeductRequestDto(
    @NotBlank(message = "상품 식별자는 필수 입니다.")
    String productId,

    @PositiveOrZero(message = "상품의 재고는 0개 이상이여야합니다.")
    Integer quantity
) {

}
