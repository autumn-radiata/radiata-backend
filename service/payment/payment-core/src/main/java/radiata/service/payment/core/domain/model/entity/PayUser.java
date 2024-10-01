package radiata.service.payment.core.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.payment.core.domain.model.vo.Money;

@Getter
@Entity
@Table(name = "r_pay_user")
@SQLRestriction("deleted_at IS NULL")
@Builder // 빌더 패턴은 테스트 용도로만 사용
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 객체는 기본 생성자를 필요로 함
public class PayUser {

    @Id
    private String id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Money balance;

    public static PayUser of(
        String id,
        String userId,
        String encodedPassword
    ) {
        return PayUser.builder()
            .id(id)
            .userId(userId)
            .password(encodedPassword)
            .balance(Money.of(0L))
            .build();
    }

    /**
     * 비밀번호 변경
     */
    public void changePassword(String newEncodedPassword) {
        this.password = newEncodedPassword;
    }

    /**
     * 입금
     */
    public void deposit(Money money) {
        this.balance = this.balance.add(money);
    }

    /**
     * 결제 금액보다 충분한 충전금이 있는지 확인
     */
    public boolean isBalanceLessThan(Money paymentAmount) {
        return this.balance.subtract(paymentAmount).isNegative();
    }

    /**
     * 출금
     */
    public void withdraw(Money money) {
        if (this.balance.subtract(money).isNegative()) {
            throw new BusinessException(ExceptionMessage.INSUFFICIENT_BALANCE);
        }

        this.balance = this.balance.subtract(money);
    }
}
