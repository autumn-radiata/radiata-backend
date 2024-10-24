package radiata.service.timesale.consumer.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import radiata.common.domain.timesale.dto.request.TimeSaleProductSaleRollbackRequestDto;
import radiata.service.timesale.core.component.DistributeLockExecutor;
import radiata.service.timesale.core.service.interfaces.TimeSaleProductService;

@Component
@Slf4j
@RequiredArgsConstructor
public class TimeSaleProductRollbackListener {

    private final TimeSaleProductService productService;
    private final DistributeLockExecutor distributeLockExecutor;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "timesale.add-stock", groupId = "timesale.add-stock.update")
    public void listen(String message) throws JsonProcessingException {

        TimeSaleProductSaleRollbackRequestDto requestDto = objectMapper.readValue(
                message, TimeSaleProductSaleRollbackRequestDto.class);

        distributeLockExecutor.execute("timeSaleProduct_sale_lock_" + requestDto.timeSaleProductId(), 1000, 1000,
                () -> productService.decrementSaleQuantity(requestDto.timeSaleProductId(), requestDto.quantity())
        );
    }
}
