//package radiata.service.gateway.core.config;
//
//import io.github.resilience4j.circuitbreaker.CircuitBreaker;
//import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
//import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
//import io.github.resilience4j.core.registry.EntryAddedEvent;
//import io.github.resilience4j.core.registry.EntryRemovedEvent;
//import io.github.resilience4j.core.registry.EntryReplacedEvent;
//import io.github.resilience4j.core.registry.RegistryEventConsumer;
//import java.time.Duration;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@Slf4j
//public class Resilience4jConfig {
//
//    //todo : record-exceptions: 서킷 브레이커가 예외로 간주할 예외클래스
//    @Bean
//    public CircuitBreakerConfig circuitBreakerConfig() {
//        return CircuitBreakerConfig.custom()
//            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)  // 슬라이딩 윈도우 타입 설정
//            .slidingWindowSize(1000)  // 슬라이딩 윈도우 크기 설정
//            .minimumNumberOfCalls(5)  // 최소 호출 수 설정
//            .slowCallRateThreshold(100)  // 느린 호출 비율 임계값 설정
//            .slowCallDurationThreshold(Duration.ofMillis(60000))  // 느린 호출 기준 시간 설정
//            .failureRateThreshold(50)  // 실패율 임계값 설정
//            .permittedNumberOfCallsInHalfOpenState(3)  // Half-open 상태에서 허용되는 최대 호출 수 설정
//            .waitDurationInOpenState(Duration.ofSeconds(20))  // Open 상태에서 Half-open 상태로 전환되기 전 대기 시간 설정
//            .build();
//    }
//
//    @Bean
//    public RegistryEventConsumer<CircuitBreaker> circuitBreakerConsumer() {
//        return new RegistryEventConsumer<CircuitBreaker>() {
//            @Override
//            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
//                CircuitBreaker circuitBreaker = entryAddedEvent.getAddedEntry();
//                circuitBreaker.getEventPublisher()
//                    .onFailureRateExceeded(event -> log.error("###서킷 브레이커 '{}'에서 실패율 {}%를 {}에서 달성하였습니다.",
//                        event.getCircuitBreakerName(), event.getFailureRate(), event.getCreationTime()))
//                    .onError(event -> log.error("###서킷 브레이커 '{}'에서 오류가 발생하였습니다.",
//                        event.getCircuitBreakerName()))
//                    .onStateTransition(
//                        event -> log.warn("###서킷 브레이커 '{}'의  상태가  '{}'에서 '{}'으로 변경되었습니다.//'{}'",
//                            event.getCircuitBreakerName(), event.getStateTransition().getFromState(),
//                            event.getStateTransition().getToState(), event.getCreationTime()));
//            }
//
//            @Override
//            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
//            }
//
//            @Override
//            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
//            }
//        };
//    }
//
//    @Bean
//    public CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig circuitBreakerConfig,
//        RegistryEventConsumer<CircuitBreaker> circuitBreakerConsumer) {
//        return CircuitBreakerRegistry.of(circuitBreakerConfig, circuitBreakerConsumer);
//    }
//}