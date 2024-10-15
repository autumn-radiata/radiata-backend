package radiata.service.user.core.domain.model.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import radiata.service.user.core.domain.model.constant.PointType;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder
@Table(name = "r_point_history")
@EntityListeners(AuditingEntityListener.class)
public class PointHistory {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private int rewardPoint;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime issueAt;

    public static PointHistory of(String id, User user, int rewardPoint, PointType pointType) {
        return PointHistory.builder()
            .id(id)
            .user(user)
            .rewardPoint(rewardPoint)
            .pointType(pointType)
            .build();
    }

}
