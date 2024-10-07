package radiata.service.gateway.core.filter;

import io.jsonwebtoken.Claims;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import radiata.common.domain.user.constant.UserRole;
import radiata.service.gateway.core.util.JwtUtil;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PreFilter implements GlobalFilter, Ordered {

    private static final Logger logger = Logger.getLogger(PreFilter.class.getName());
    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        logger.info("Pre filter :Request URI is" + path);

        //회원가입과 로그인 시 토큰 검증 하지 않음.
        if (path.startsWith("/auth")) {
            logger.info("token issue: " + jwtUtil.createToken("username", UserRole.CUSTOMER));
            return chain.filter(exchange);
        }
        //토큰 추출
        String token = jwtUtil.parseToken(exchange);

        // 토큰 유효성 검증과 만료기간 확인
        Claims claims = jwtUtil.tokenParseClaims(token);

        //헤더에 값을 추가
        exchange.getRequest().mutate()
            .header("X-User-Name", jwtUtil.getUsername(claims))
            .header("X-User-Roles", jwtUtil.getRole(claims))
            .build();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
