package radiata.common.domain.timesale.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Range;

public record TimeSaleProductCreateRequestDto(

    @NotBlank(message = "상품 ID 를 입력해 주세요.")
    String productId,

    @NotBlank(message = "타임 세일 ID 를 입력해 주세요.")
    String timeSaleId,

    @Range(min = 1, max = 100, message = "할인율은 1 ~ 100 까지 입력하실 수 있습니다.")
    @NotNull(message = "할인율을 입력해주세요")
    Integer discountRate,

    @NotNull(message = "판매하실 재고 수량을 입력해 주세요.")
    @Positive(message = "판매하실 재고 수량은 음수가 될 수 없습니다.")
    Integer totalQuantity,

    @NotNull(message = "타임 세일 시작 시간을 입력해 주세요.")
    LocalDateTime timeSaleStartTime,

    @NotNull(message = "타임 세일 종료 시간을 입력해 주세요.")
    LocalDateTime timeSaleEndTime
) {

}
