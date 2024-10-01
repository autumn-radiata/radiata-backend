package radiata.service.coupon.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "radiata.database")
public class CouponCoreApplication {

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}