package radiata.common.domain.order.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

public record OrderCreateRequestDto(
    String address,
    String comment,

    @PositiveOrZero(message = "적립금은 0원 이상이어야 합니다.")
    Integer point,

    List<OrderItemCreateRequestDto> itemList) {

}
