package radiata.service.timesale.core.domain;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import radiata.common.domain.timesale.dto.request.TimeSaleCreateRequestDto;

@Entity
@Getter
@Builder
@SQLRestriction("deleted_at IS NULL")
@Table(name = "r_timesale")
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class TimeSale extends BaseEntity {

    @Id
    private String id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "timesale_start_date", nullable = false)
    private LocalDateTime timeSaleStartDate;

    @Column(name = "timesale_end_date", nullable = false)
    private LocalDateTime timeSaleEndDate;

    @OneToMany(mappedBy = "timeSale", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<TimeSaleProduct> timeSaleProducts;

    public static TimeSale of(String id, String title, LocalDateTime timeSaleStartDate, LocalDateTime timeSaleEndDate) {

        return TimeSale.builder()
            .id(id)
            .title(title)
            .timeSaleStartDate(timeSaleStartDate)
            .timeSaleEndDate(timeSaleEndDate)
            .build();
    }
}
