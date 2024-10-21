package radiata.service.coupon.consumer.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import radiata.common.domain.coupon.dto.CouponIssueRequestDto;
import radiata.service.coupon.core.service.interfaces.coupon_issue.CouponIssueService;

@Component
@Slf4j
@RequiredArgsConstructor
public class CouponIssueListener {

    private final CouponIssueService couponIssueService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "coupon.issue", groupId = "coupon.issue.create")
    public void listen(String message) throws JsonProcessingException {

        log.info("Received message {}", message);
        CouponIssueRequestDto couponIssueRequestDto = objectMapper.readValue(message,
                CouponIssueRequestDto.class);

        couponIssueService.issue(couponIssueRequestDto.couponId(), couponIssueRequestDto.userId());
    }
}
