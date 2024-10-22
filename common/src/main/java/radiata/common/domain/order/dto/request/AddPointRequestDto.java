package radiata.common.domain.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AddPointRequestDto(
    @NotBlank(message = "userId는 필수 입니다.")
    String userId,

    @NotBlank(message = "포인트 금액은 필수 입니다.")
    @Min(value = 1, message = "포인트 금액은 1원 이상이어야 합니다.")
    Integer point
) {

}
