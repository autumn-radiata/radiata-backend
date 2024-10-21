package radiata.common.domain.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AddStockRequestDto(

    @NotBlank(message = "ID는 필수 입니다.")
    String id,

    @NotBlank(message = "수량은 필수 입니다.")
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    Integer quantity
) {

}
