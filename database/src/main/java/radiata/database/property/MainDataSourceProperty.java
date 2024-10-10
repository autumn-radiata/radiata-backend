package radiata.database.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.datasource.hikari.main")
public record MainDataSourceProperty(
    String url,
    String username,
    String password,
    String driverClassName
) {

}
