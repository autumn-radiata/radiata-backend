package radiata.service.user.core.domain.model.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import radiata.service.user.core.domain.model.vo.PointType;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder
@SQLRestriction("deleted_at IS NULL")
@Table(name = "r_point_history")
public class PointHistory {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false)
    private int rewardPoint;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointType pointType;

    //todo :  baseEntity 추가후 @CreationTimestamp로 초기화
    @Column
    private LocalDateTime issueAt;

    public static PointHistory of(User user, int rewardPoint, PointType pointType) {
        return PointHistory.builder()
            .user(user)
            .rewardPoint(rewardPoint)
            .pointType(pointType)
            .build();
    }

}
