package radiata.common.domain.timesale.dto.request;

import java.time.LocalDateTime;

public record TimeSaleCreateRequestDto(
    String title,
    LocalDateTime timeSaleStartDate,
    LocalDateTime timeSaleEndDate
) {

}
