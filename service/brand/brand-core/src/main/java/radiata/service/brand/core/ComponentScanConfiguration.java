package radiata.service.brand.core;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * JpaRepository 빈 조회를 위한 ComponentScan 설정
 */
@EnableAutoConfiguration
@EnableFeignClients
@ComponentScan
@Configuration
public class ComponentScanConfiguration {

}
