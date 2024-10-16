package radiata.common.domain.timesale.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record TimeSaleCreateRequestDto(
    @NotBlank(message = "타임세일명을 입력해주세요.")
    String title,
    @NotNull(message = "타임세일명 시작일을 입력해주세요.")
    LocalDateTime timeSaleStartDate,
    @NotNull(message = "타임세일명 종료일을 입력해주세요.")
    LocalDateTime timeSaleEndDate
) {

}
