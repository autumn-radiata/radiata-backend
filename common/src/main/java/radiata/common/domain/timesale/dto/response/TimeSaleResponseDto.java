package radiata.common.domain.timesale.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TimeSaleResponseDto(
    String timeSaleId,
    String title,
    LocalDateTime timeSaleStartDate,
    LocalDateTime timeSaleEndDate
) {

    public static TimeSaleResponseDto of(String timeSaleId, String title, LocalDateTime timeSaleStartDate, LocalDateTime timeSaleEndDate) {

        return TimeSaleResponseDto.builder()
            .timeSaleId(timeSaleId)
            .title(title)
            .timeSaleStartDate(timeSaleStartDate)
            .timeSaleEndDate(timeSaleEndDate)
            .build();
    }

}
