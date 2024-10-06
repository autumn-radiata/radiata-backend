package radiata.service.payment.api.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.domain.payment.dto.request.TossPaymentCreateRequestDto;
import radiata.common.domain.payment.dto.response.TossPaymentCreateResponseDto;
import radiata.common.message.SuccessMessage;
import radiata.common.response.SuccessResponse;
import radiata.service.payment.core.service.TossPaymentService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final TossPaymentService tossPaymentService;

    @PostMapping("/toss")
    public SuccessResponse<TossPaymentCreateResponseDto> requestTossPayment(
        @RequestBody TossPaymentCreateRequestDto request
    ) {
        return SuccessResponse.success(SuccessMessage.OK.getMessage(), tossPaymentService.requestTossPayment(request));
    }
}
