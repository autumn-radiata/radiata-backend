package radiata.service.order.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"radiata.service.order"})
public class OrderApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApiApplication.class, args);
    }
}