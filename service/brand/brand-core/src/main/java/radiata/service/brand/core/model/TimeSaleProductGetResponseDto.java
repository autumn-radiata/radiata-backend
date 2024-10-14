package radiata.service.brand.core.model;

import java.time.LocalDateTime;

public record TimeSaleProductGetResponseDto(
    String productId,
    Integer discountRate,
    LocalDateTime timeSaleStartTime,
    LocalDateTime timeSaleEndTime

) {

}
