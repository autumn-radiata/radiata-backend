package radiata.service.payment.core.domain.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable // JPA 값객체 어노테이션
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 엔티티, 임베디드는 기본 생성자 필수
public class Account {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Bank bank;

    @Column(nullable = false)
    private String accountNumber;

    public static Account of(Bank bank, String accountNumber) {
        Account account = new Account();
        account.bank = bank;
        account.accountNumber = accountNumber;
        return account;
    }

    @Override
    public String toString() {
        return bank.getName() + " " + accountNumber;
    }
}
