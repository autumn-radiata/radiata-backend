package radiata.common.domain.order.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreateRequestDto {

    private String address;
    private String comment;
    private List<OrderItemCreateRequestDto> itemList;
}
