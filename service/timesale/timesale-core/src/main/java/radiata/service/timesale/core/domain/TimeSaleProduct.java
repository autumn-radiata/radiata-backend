package radiata.service.timesale.core.domain;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@SQLRestriction("deleted_at IS NULL")
@Table(name = "r_timesale_product")
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class TimeSaleProduct extends BaseEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String productId;

    @ManyToOne
    @JoinColumn(name = "timesale_id", nullable = false)
    private TimeSale timeSale;

    @Column(nullable = false)
    private Integer discountRate;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "timesale_start_time", nullable = false)
    private LocalDateTime timeSaleStartTime;

    @Column(name = "timesale_end_time", nullable = false)
    private LocalDateTime timeSaleEndTime;
}
