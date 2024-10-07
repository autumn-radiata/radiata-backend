package radiata.service.gateway.core.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import radiata.common.domain.user.constant.UserRole;

@Component
@Slf4j
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int ACCESS_EXPIRATION = 60 * 60;
    private final SecretKey secretKey;
    @Value("${spring.application.name}")
    private String issuer;


    public JwtUtil() {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(
            "401b09eab3c013d4ca54922bb802bec8fd5318192b0a75f201d8b3727429080fb337591abd3e44453b954555b7a0812e1081c39b740293f765eae731f5a65ed1"));
    }


    public String createToken(String username, UserRole role) {
        return BEARER_PREFIX + Jwts.builder()
            .subject(username)
            .claim("role", role.name())
            .issuer(issuer)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + Duration.ofSeconds(ACCESS_EXPIRATION).toMillis()))
            .signWith(secretKey)
            .compact();
    }
    //todo : globalException처리하고 null로직 모두 삭제

    //토큰 유효성 검증
    private Claims validate(String token) {
        try {
            if (token != null) {
                return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
            }
            return null;
            //todo : SignatureException,MalformedJwtException,ExpiredJwtException 예외처리
        } catch (Exception e) {
            log.warn("SignatureException");
            throw e;
        }
    }

    //Claims 만료 여부 확인
    private boolean checkExpiration(Claims claims) {
        return claims.getExpiration().after(new Date());
    }

    //token을 claims로 변환
    public Claims tokenParseClaims(String token) {
        Claims claims = validate(token);
        assert claims != null;
        if (checkExpiration(claims)) {
            return claims;
        }
        return null;
    }

    //jwt 추출
    public String parseToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getUsername(Claims claims) {
        return claims.getSubject();
    }

    public String getRole(Claims claims) {
        return (String) claims.get("role");
    }


}