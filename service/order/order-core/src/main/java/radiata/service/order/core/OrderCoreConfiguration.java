package radiata.service.order.core;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

// JpaRepository 빈 조회를 위한 ComponentScan 설정
@EnableAutoConfiguration
@EnableFeignClients
@EnableScheduling
@Configuration
public class OrderCoreConfiguration {

}

