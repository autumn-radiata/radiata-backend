package radiata.service.payment.api.batch;

import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.transaction.PlatformTransactionManager;
import radiata.common.domain.payment.constant.PaymentStatus;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SettleJob {

    private static final int PAGE_SIZE = 500;
    private static final String JOB_NAME = "paymentJob";

    private final DataSource dataSource;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean(JOB_NAME)
    public Job paymentJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
            .start(paymentStep())
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean(JOB_NAME + "_step")
    @JobScope
    public Step paymentStep() {
        return new StepBuilder(JOB_NAME + "_step", jobRepository)
            .<ApprovedPayment, ApprovedPayment>chunk(PAGE_SIZE, transactionManager)
            .reader(paymentReader())
            .writer(paymentWriter())
            .build();
    }

    @Bean(JOB_NAME + "_reader")
    @StepScope
    public ItemReader<ApprovedPayment> paymentReader() {
        return new JdbcPagingItemReaderBuilder<ApprovedPayment>()
            .name(JOB_NAME + "_reader")
            .dataSource(dataSource)
            .fetchSize(PAGE_SIZE)
            .selectClause("SELECT id, amount, status, created_at")
            .fromClause("FROM r_payment")
            .whereClause("WHERE status = :status")
            .parameterValues(Map.of("status", PaymentStatus.APPROVED.name()))
            .rowMapper((rs, rowNum) -> new ApprovedPayment(rs.getString("id")))
            .sortKeys(Map.of("id", Order.ASCENDING))
            .build();
    }

    @Bean(JOB_NAME + "_writer")
    @StepScope
    public ItemWriter<ApprovedPayment> paymentWriter() {
        return new JdbcBatchItemWriterBuilder<ApprovedPayment>()
            .dataSource(dataSource)
            .sql("UPDATE r_payment SET status = :status WHERE id = :id")
            .itemSqlParameterSourceProvider(item -> {
                MapSqlParameterSource param = new MapSqlParameterSource();
                param.addValue("id", item.id());
                param.addValue("status", PaymentStatus.SETTLED.name());
                return param;
            })
            .build();
    }
}
