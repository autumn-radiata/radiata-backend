package radiata.service.payment.core.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import radiata.database.model.BaseEntity;
import radiata.service.payment.core.domain.model.vo.Money;

@Getter
@Entity
@Table(name = "r_money_change_history")
@SQLRestriction("deleted_at IS NULL")
@Builder // 빌더 패턴은 테스트 용도로만 사용
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 객체는 기본 생성자를 필요로 함
public class MoneyChangeHistory extends BaseEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private Money amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_user_id", nullable = false)
    private PayUser payUser;

    public static MoneyChangeHistory of(
        String id,
        Money amount,
        PayUser payUser
    ) {
        return MoneyChangeHistory.builder()
            .id(id)
            .amount(amount)
            .payUser(payUser)
            .build();
    }
}
