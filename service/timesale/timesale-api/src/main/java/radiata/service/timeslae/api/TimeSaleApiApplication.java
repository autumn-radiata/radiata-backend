package radiata.service.timeslae.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import radiata.service.timeslae.api.controller.TimeSaleController;
import radiata.service.timeslae.api.controller.TimeSaleProductController;
import radiata.service.timeslae.api.service.TimeSaleApiService;
import radiata.service.timeslae.api.service.TimeSaleProductApiService;

@SpringBootApplication(
    scanBasePackages = {
        "radiata.service.timesale"
    },
    scanBasePackageClasses = {
        TimeSaleController.class,
        TimeSaleApiService.class,
        TimeSaleProductController.class,
        TimeSaleProductApiService.class
    }
)
public class TimeSaleApiApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-database-dev,application-timesale-api");
        SpringApplication.run(TimeSaleApiApplication.class, args);
    }
}