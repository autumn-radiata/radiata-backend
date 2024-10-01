package radiata.common.domain.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemCreateRequestDto {
    private String productId;
    private String timesaleProductId;
    private String couponIssuedId;
    private String rewardPointId;
    private Integer quantity;
//    private ProductSize size;
}
