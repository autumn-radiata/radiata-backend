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
            .route("user-service", r -> r.path("/auth/**", "/users/**")
                .filters(f -> applyCommonFilters(f, "user-service"))
                .uri("lb://user-service"))
            .route("order-service", r -> r.path("/orders/**")
                .filters(f -> applyCommonFilters(f, "order-service"))
                .uri("lb://order-service"))
            .route("brand-service", r -> r.path("/goods/**", "/brands/**", "/categories/**")
                .filters(f -> applyCommonFilters(f, "brand-service"))
                .uri("lb://brand-service"))
            //todo : /products 경로 겹침 >>products 변경 부탁드려용
            .route("timesale-service", r -> r.path("/timesales/**", "/timesale-products/**", "/products/**")
                .filters(f -> applyCommonFilters(f, "timesale-service"))
                .uri("lb://timesale-service"))
            .route("payment-service", r -> r.path("/payments/**")
                .filters(f -> applyCommonFilters(f, "payment-service"))
                .uri("lb://payment-service"))
            .route("coupon-service", r -> r.path("/coupons/**", "/couponissues/**")
                .filters(f -> applyCommonFilters(f, "coupon-service"))
                .uri("lb://coupon-service"))
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
