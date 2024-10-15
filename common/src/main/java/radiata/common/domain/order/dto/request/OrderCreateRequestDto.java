package radiata.common.domain.order.dto.request;

import java.util.List;

public record OrderCreateRequestDto(
    String address,
    String comment,
    Integer point,
    List<OrderItemCreateRequestDto> itemList) {

}
