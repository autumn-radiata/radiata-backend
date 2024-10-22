package radiata.service.order.core.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import radiata.common.domain.order.dto.request.AddPointRequestDto;
import radiata.common.domain.order.dto.request.AddStockRequestDto;

@Configuration
public class KafkaConfig {

    @Value(value = "${spring.kafka.broker.url}")
    private String brokerUrl;

    // 쿠폰 상태 변경 & 결제 취소 요청
    @Bean
    public ProducerFactory<String, String> cancelRequestProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUrl);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> cancelRequestKafkaTemplate() {
        return new KafkaTemplate<>(cancelRequestProducerFactory());
    }

    // 타임세일 상품 & 상품 재고 증감 요청
    @Bean
    public ProducerFactory<String, AddStockRequestDto> addStockProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUrl);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, AddStockRequestDto> addStockKafkaTemplate() {
        return new KafkaTemplate<>(addStockProducerFactory());
    }

    // 적립금 증감 요청
    @Bean
    public ProducerFactory<String, AddPointRequestDto> addPointProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUrl);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, AddPointRequestDto> addPointKafkaTemplate() {
        return new KafkaTemplate<>(addPointProducerFactory());
    }
}
