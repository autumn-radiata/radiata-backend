package radiata.service.brand.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"radiata.service.brand", "radiata.database", "radiata.common"})
@ConfigurationPropertiesScan(basePackages = {"radiata.service.brand", "radiata.database", "radiata.common"})
public class BrandApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrandApplication.class, args);
    }
}