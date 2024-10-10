package radiata.database.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.datasource.hikari.batch")
public record BatchDataSourceProperty(
    String url,
    String username,
    String password,
    String driverClassName
) {

}
