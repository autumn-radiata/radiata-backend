package radiata.service.payment.core.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable // JPA 값객체 어노테이션
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 엔티티, 임베디드는 기본 생성자 필수
public class Money {

    public static final Money ZERO = new Money(0L);

    private Long amount;

    public Money(long amount) {
        this.amount = amount;
    }

    public static Money of(long amount) {
        return new Money(amount);
    }

    public Money add(Money money) {
        return new Money(this.amount + money.amount);
    }

    public Money subtract(Money money) {
        return new Money(this.amount - money.amount);
    }

    public Money multiply(int multiplier) {
        return new Money(this.amount * multiplier);
    }

    public boolean isNegative() {
        return amount < 0;
    }

    @Override
    public String toString() {
        return String.valueOf(amount);
    }
}
