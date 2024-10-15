package radiata.common.domain.timesale.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TimeSaleProductResponseDto(
    String timeSaleProductId,
    String productId,
    Integer discountRate,
    Integer saleQuantity,
    Integer totalQuantity,
    LocalDateTime timeSaleStartTime,
    LocalDateTime timeSaleEndTime
) implements Serializable {

}