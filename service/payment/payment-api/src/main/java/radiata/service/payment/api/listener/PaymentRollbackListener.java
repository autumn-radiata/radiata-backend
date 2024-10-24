package radiata.service.payment.api.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import radiata.common.domain.payment.dto.request.PaymentCancelRequestDto;
import radiata.service.payment.core.service.PaymentRollbackService;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRollbackListener {

    private final ObjectMapper objectMapper;
    private final PaymentRollbackService paymentRollbackService;

    @KafkaListener(topics = "payment.cancel", groupId = "payment.cancel.update")
    public void cancelPayment(String message) throws JsonProcessingException {
        PaymentCancelRequestDto request = objectMapper.readValue(message, PaymentCancelRequestDto.class);
        paymentRollbackService.rollbackPayment(request.paymentId());
    }
}
