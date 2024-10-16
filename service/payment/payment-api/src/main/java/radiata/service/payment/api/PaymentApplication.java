package radiata.service.payment.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"radiata.service.payment", "radiata.database", "radiata.common"})
@ConfigurationPropertiesScan(basePackages = {"radiata.service.payment", "radiata.database", "radiata.common"})
public class PaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
}