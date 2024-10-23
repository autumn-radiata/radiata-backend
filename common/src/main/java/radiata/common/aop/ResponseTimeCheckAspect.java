package radiata.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ResponseTimeCheckAspect {

    @Around("@annotation(ResponseTimeCheck)")
    public Object checkResponseTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object obj = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        double responseTimeInSeconds = responseTime / 1000.0; // 밀리초를 초 단위로 변환

        log.info("----- Response Time: {} seconds - Method: {} -----",
                String.format("%.3f", responseTimeInSeconds),
                joinPoint.getSignature().getName());

        return obj;
    }
}
