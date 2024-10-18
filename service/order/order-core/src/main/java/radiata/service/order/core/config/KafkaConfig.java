package radiata.service.order.core.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
// TODO - 정해지면 종류 별(결제, 타임세일 상품, 발급 쿠폰, 상품, 적립금(유저))로 등록
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, String> paymentRollbackProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        // TODO - 포트 주소 "yunjae.click"으로 수정 예정
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> paymentRollbackKafkaTemplate() {
        return new KafkaTemplate<>(paymentRollbackProducerFactory());
    }
}
