package radiata.service.coupon.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = {
        "radiata.service.coupon"
    }
)
public class CouponApiApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-database-dev,application-api");
        SpringApplication.run(CouponApiApplication.class, args);
    }
}