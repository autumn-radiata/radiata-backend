package radiata.service.coupon.consumer.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import radiata.common.domain.coupon.dto.request.CouponIssueRollbackRequestDto;
import radiata.service.coupon.core.service.interfaces.coupon_issue.CouponIssueService;

@Component
@Slf4j
@RequiredArgsConstructor
public class CouponIssueRollbackListener {

    private final CouponIssueService couponIssueService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "coupon.rollback-status", groupId = "coupon.rollback-status.update")
    public void CouponIssueRollback(String message) throws JsonProcessingException {

        CouponIssueRollbackRequestDto requestDto = objectMapper.readValue(message,
                CouponIssueRollbackRequestDto.class);

        couponIssueService.rollbackCouponIssue(requestDto.couponIssueId(), requestDto.couponStatus());
    }
}
