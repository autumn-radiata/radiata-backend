package radiata.common.domain.order.dto.response;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponseDto implements Serializable {

    private String orderId;
    private String userId;
    private String status;
    private Integer orderPrice;
    private Boolean isRefunded;
    private String address;
    private String paymentId;
//    private String paymentType;
    private String comment;
    private List<OrderItemResponseDto> itemList;

}
