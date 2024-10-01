package radiata.common.domain.order.dto.request;

import java.util.List;

public record OrderCreateRequestDto(
    String address,
    String comment,
    List<OrderItemCreateRequestDto> itemList) {

}
