package radiata.service.brand.core.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        //key-string,value itemdto를 사용
        RedisTemplate<String, Object> template = new RedisTemplate<>();  // 1
        //redis와 연결을 하는 redisconnectFactory를 전달 이는 yml의 내용을 바탕으로 bean객체로 등록
        template.setConnectionFactory(connectionFactory);
        //데이터 직렬 방법 결정
        //key - 문자열로 직렬화 역직렬화
        template.setKeySerializer(RedisSerializer.string());
        //value - json으로 직렬화 역직렬화
        template.setValueSerializer(RedisSerializer.json());
        return template;
    }

}
