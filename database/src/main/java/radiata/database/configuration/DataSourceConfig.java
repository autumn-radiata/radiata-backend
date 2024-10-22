package radiata.database.configuration;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import radiata.database.property.BatchDataSourceProperty;
import radiata.database.property.MainDataSourceProperty;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final MainDataSourceProperty mainDataSourceProperty;
    private final BatchDataSourceProperty batchDataSourceProperty;

    /**
     * 메인 DB 데이터소스
     */
    @Bean("dataSource")
    @Primary
    public DataSource mainDataSource() {
        HikariDataSource dataSource = DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .driverClassName(mainDataSourceProperty.driverClassName())
            .url(mainDataSourceProperty.url())
            .username(mainDataSourceProperty.username())
            .password(mainDataSourceProperty.password())
            .build();

        dataSource.setMaximumPoolSize(300);

        return new LazyConnectionDataSourceProxy(dataSource); // 트랜잭션 진입 하더라도 커넥션 가져오지 않음
    }

    /**
     * 배치 메타데이터 저장용 데이터소스
     */
    @Bean("batchDataSource")
    public DataSource batchDataSource() {
        HikariDataSource dataSource = DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .driverClassName(batchDataSourceProperty.driverClassName())
            .url(batchDataSourceProperty.url())
            .username(batchDataSourceProperty.username())
            .password(batchDataSourceProperty.password())
            .build();

        dataSource.setMaximumPoolSize(1);

        return new LazyConnectionDataSourceProxy(dataSource); // 트랜잭션 진입 하더라도 커넥션 가져오지 않음
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }
}
