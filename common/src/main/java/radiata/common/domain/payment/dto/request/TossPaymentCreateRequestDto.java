package radiata.common.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record TossPaymentCreateRequestDto(

    @NotBlank(message = "userId는 필수입니다.")
    String userId,

    @NotBlank(message = "orderId는 필수입니다.")
    String orderId,

    @NotBlank(message = "paymentKey는 필수입니다.")
    String paymentKey,

    @NotNull(message = "amount는 필수입니다.")
    @PositiveOrZero(message = "amount는 0 이상이어야 합니다.")
    Long amount
) {

}
