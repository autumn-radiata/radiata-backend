package radiata.service.user.core.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${spring.application.name}")
    private String applicationName;


    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, applicationName + "-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, Object> batchConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, applicationName + "-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        //컨슈머가 메시지를 가져오기 위해 브로커에게 poll() 요청을 보낼 때, 한번에 10개의 레코드를 소비함.
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        //설정된 값보다 데이터 크기가 적은 경우 응답을 기다리는 최대 시간/ms
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 8000);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean("backKafkaListenerContainerFactory")
    ConcurrentKafkaListenerContainerFactory<String, Object> batchKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        ContainerProperties containerProperties = factory.getContainerProperties();
        containerProperties.setAckMode(AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setIdleBetweenPolls(6000);
        //리스너를 배치 모드로 동작
        factory.setBatchListener(true);
        factory.setConsumerFactory(batchConsumerFactory());
        factory.setConcurrency(2);
        //연속되는 poll사이의 최대 대기 시간을 설정

        return factory;
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> KafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
