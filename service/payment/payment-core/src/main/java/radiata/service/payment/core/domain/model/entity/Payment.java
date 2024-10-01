package radiata.service.payment.core.domain.model.entity;

import com.github.ksuid.Ksuid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import radiata.service.payment.core.domain.model.vo.Money;
import radiata.service.payment.core.domain.model.vo.PaymentStatus;
import radiata.service.payment.core.domain.model.vo.PaymentType;

@Getter
@Entity
@Table(name = "r_payment")
@SQLRestriction("deleted_at IS NOT NULL")
@Builder // 빌더 패턴은 테스트 용도로만 사용
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 객체는 기본 생성자를 필요로 함
public class Payment {

    @Id
    private String id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String transactionId; // 각 결제 수단 별 고유한 결제 번호

    @Column(nullable = false)
    private Money amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType type;

    private LocalDateTime approvedAt;

    public static Payment of(
        String id,
        String userId,
        String transactionId,
        Money amount,
        PaymentType type
    ) {
        return Payment.builder()
            .id(id)
            .userId(userId)
            .transactionId(transactionId)
            .amount(amount)
            .type(type)
            .status(PaymentStatus.PENDING)
            .build();
    }

    /**
     * 결제 승인
     */
    public void approve() {
        this.status = PaymentStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
    }

    /**
     * 결제 실패
     */
    public void fail() {
        this.status = PaymentStatus.FAILED;
    }

    /**
     * 정산
     */
    public void settle() {
        this.status = PaymentStatus.SETTLED;
    }
}
