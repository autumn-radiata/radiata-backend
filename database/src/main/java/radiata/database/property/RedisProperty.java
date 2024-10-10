package radiata.database.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.data.redis")
public record RedisProperty(
    String host,
    Integer port
) {

}
