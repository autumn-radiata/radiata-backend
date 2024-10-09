package radiata.service.gateway.core.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("user-service", r -> r.path("/auth/**", "/user/**")
                .filters(f -> f
                        .circuitBreaker(c -> c
                            .setName("user-service")
                            .setFallbackUri("forward:/Fallback")
                        )
                    //todo : time out 성능 문제를 해결한 후 추가
                    /* .retry(c -> c
                        .setRetries(3)
                        .setSeries(HttpStatus.Series.SERVER_ERROR)
                        .setMethods(HttpMethod.GET, HttpMethod.POST))*/
                )
                .uri("lb://user-service")
            )
            .build();
    }

}
