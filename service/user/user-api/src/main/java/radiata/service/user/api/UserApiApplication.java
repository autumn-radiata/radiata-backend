package radiata.service.user.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"radiata.service.user", "radiata.database", "radiata.database"})
@ConfigurationPropertiesScan(basePackages = {"radiata.service.user", "radiata.database", "radiata.database"})
public class UserApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApiApplication.class, args);
    }

}
