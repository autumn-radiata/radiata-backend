package radiata.service.timesale.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(
        scanBasePackages = {
                "radiata.service.timesale", "radiata.database", "radiata.common"
        }
)
@ConfigurationPropertiesScan(basePackages = {"radiata.service.timesale", "radiata.database", "radiata.common"})
public class TimeSaleConsumerApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-database-dev,application-consumer");
        SpringApplication.run(TimeSaleConsumerApplication.class, args);
    }
}