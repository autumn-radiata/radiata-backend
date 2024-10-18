package radiata.common.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderTossPaymentRequestDto(

    @NotBlank(message = "paymentKey는 필수입니다.")
    String paymentKeyId,

    @NotNull(message = "amount는 필수입니다.")
    @PositiveOrZero(message = "amount는 0 이상이어야 합니다.")
    Integer amount
) {

}
