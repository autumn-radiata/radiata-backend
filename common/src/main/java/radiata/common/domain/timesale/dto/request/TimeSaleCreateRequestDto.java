package radiata.common.domain.timesale.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record TimeSaleCreateRequestDto(
    @NotBlank(message = "타임세일명을 입력해주세요.")
    String title,
    @NotBlank(message = "타임세일명 시작일을 입력해주세요.")
    LocalDateTime timeSaleStartDate,
    @NotBlank(message = "타임세일명 종료일을 입력해주세요.")
    LocalDateTime timeSaleEndDate
) {

}
