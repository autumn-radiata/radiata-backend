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
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;

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
    private Integer saleQuantity;

    @Column(nullable = false)
    private Integer totalQuantity;

    @Column(name = "timesale_start_time", nullable = false)
    private LocalDateTime timeSaleStartTime;

    @Column(name = "timesale_end_time", nullable = false)
    private LocalDateTime timeSaleEndTime;

    public static TimeSaleProduct of(String id, String productId, Integer discountRate, Integer saleQuantity,
        Integer totalQuantity, LocalDateTime timeSaleStartTime, LocalDateTime timeSaleEndTime) {

        return TimeSaleProduct.builder()
            .id(id)
            .productId(productId)
            .discountRate(discountRate)
            .saleQuantity(saleQuantity)
            .totalQuantity(totalQuantity)
            .timeSaleStartTime(timeSaleStartTime)
            .timeSaleEndTime(timeSaleEndTime)
            .build();
    }

    public void sale() {

        if (!availableSaleTime()) {
            throw new BusinessException(ExceptionMessage.TIME_SALE_PRODUCT_PERIOD);
        }

        if (!availableSaleQuantity()) {
            throw new BusinessException(ExceptionMessage.TIME_SALE_PRODUCT_LIMITED_SALE);
        }

        this.saleQuantity++;
    }

    public boolean availableSaleTime() {

        LocalDateTime now = LocalDateTime.now();

        return (now.isAfter(timeSaleStartTime) || now.isEqual(timeSaleStartTime)) && now.isBefore(timeSaleEndTime);
    }

    public boolean availableSaleQuantity() {

        return saleQuantity < totalQuantity;
    }

    public void addTimeSale(TimeSale timeSale) {
        this.timeSale = timeSale;
    }
}
