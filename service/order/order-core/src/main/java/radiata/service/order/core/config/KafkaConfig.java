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
import radiata.common.domain.order.dto.request.AddPointRequestDto;
import radiata.common.domain.order.dto.request.AddStockRequestDto;

@Configuration
// TODO - 정해지면 종류 별(결제, 타임세일 상품, 발급 쿠폰, 상품, 적립금(유저))로 등록
public class KafkaConfig {

    // 쿠폰 상태 변경 & 결제 취소 요청
    @Bean
    public ProducerFactory<String, String> cancelRequestProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        // TODO - 포트 주소 "yunjae.click"으로 수정 예정
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
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
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
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
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, AddPointRequestDto> addPointKafkaTemplate() {
        return new KafkaTemplate<>(addPointProducerFactory());
    }
}
