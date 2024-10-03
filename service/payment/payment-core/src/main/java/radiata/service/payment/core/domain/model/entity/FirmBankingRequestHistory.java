package radiata.service.payment.core.domain.model.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import radiata.service.payment.core.domain.model.vo.Account;
import radiata.service.payment.core.domain.model.vo.FirmBankingRequestStatus;
import radiata.service.payment.core.domain.model.vo.Money;

@Getter
@Entity
@Table(name = "r_firm_banking_request_history")
@SQLRestriction("deleted_at IS NULL")
@Builder // 빌더 패턴은 테스트 용도로만 사용
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 객체는 기본 생성자를 필요로 함
public class FirmBankingRequestHistory {

    @Id
    private String id;

    @Column(nullable = false)
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FirmBankingRequestStatus status;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "bank", column = @Column(name = "from_bank")),
        @AttributeOverride(name = "accountNumber", column = @Column(name = "from_account_umber")),
    })
    private Account fromAccount;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "bank", column = @Column(name = "to_bank")),
        @AttributeOverride(name = "accountNumber", column = @Column(name = "to_account_umber")),
    })
    private Account toAccount;

    public static FirmBankingRequestHistory of(
        String id,
        Money amount,
        FirmBankingRequestStatus status,
        Account fromAccount,
        Account toAccount
    ) {
        return FirmBankingRequestHistory.builder()
            .id(id)
            .amount(amount)
            .status(status)
            .fromAccount(fromAccount)
            .toAccount(toAccount)
            .build();
    }
}
