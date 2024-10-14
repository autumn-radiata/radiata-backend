package radiata.service.gateway.core.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;

@RestController
@Slf4j
public class FallbackController {

    @RequestMapping(value = "/fallBack")
    public void fallBack() {
        log.info("서킷 브레이터 fallBack method 응답데이터 반환");
        throw new BusinessException(ExceptionMessage.GATEWAY_INVALID_CONNECTED);
    }
}
