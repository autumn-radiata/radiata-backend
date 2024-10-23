package radiata.service.order.core.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import radiata.common.domain.payment.dto.request.EasyPayCreateRequestDto;
import radiata.common.domain.payment.dto.request.TossPaymentCreateRequestDto;
import radiata.common.domain.payment.dto.response.PaymentCreateResponseDto;
import radiata.common.response.SuccessResponse;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/payments/toss")
    SuccessResponse<PaymentCreateResponseDto> requestTossPayment(@RequestBody TossPaymentCreateRequestDto requestDto);

    @PostMapping("/payments/easypay")
    SuccessResponse<PaymentCreateResponseDto> requestEasyPay(@RequestBody EasyPayCreateRequestDto requestDto);
}
