package radiata.service.gateway.core.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("user-service", r -> r.path("/auth/**", "users/**")
                .filters(f -> applyCommonFilters(f, "user-service"))
                .uri("lb://user-service"))
            .route("order-service", r -> r.path("/orders/**")
                .filters(f -> applyCommonFilters(f, "order-service"))
                .uri("lb://order-service"))
            .build();
    }

    private GatewayFilterSpec applyCommonFilters(GatewayFilterSpec f, String serviceName) {
        return f.circuitBreaker(c -> c
                .setName(serviceName)
                .setFallbackUri("forward:/fallBack")
            )
            .retry(c -> c
                .setRetries(3)
                .setSeries(HttpStatus.Series.SERVER_ERROR)
                .setMethods(HttpMethod.GET));
    }

}
