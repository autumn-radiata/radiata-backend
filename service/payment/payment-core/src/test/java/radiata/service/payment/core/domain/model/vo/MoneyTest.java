package radiata.service.payment.core.domain.model.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Money 값객체 테스트")
class MoneyTest {

    @Test
    @DisplayName("Money 생성 테스트")
    void create() {
        // given
        Money money = Money.of(1000L);

        // when
        Long amount = money.getAmount();

        // then
        assertThat(amount).isEqualTo(1000L);
    }

    @Test
    @DisplayName("Money 더하기 테스트")
    void add() {
        // given
        Money money = Money.of(1000L);

        // when
        Money result = money.add(Money.of(1000L));

        // then
        assertThat(result.getAmount()).isEqualTo(2000L);
    }

    @Test
    @DisplayName("Money 빼기 테스트")
    void subtract() {
        // given
        Money money = Money.of(1000L);

        // when
        Money result = money.subtract(Money.of(1000L));

        // then
        assertThat(result.getAmount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Money 곱하기 테스트")
    void multiply() {
        // given
        Money money = Money.of(1000L);

        // when
        Money result = money.multiply(2);

        // then
        assertThat(result.getAmount()).isEqualTo(2000L);
    }

    @Test
    @DisplayName("Money 음수 여부 테스트")
    void isNegative() {
        // given
        Money money = Money.of(-1000L);

        // when
        boolean result = money.isNegative();

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Money 문자열 변환 테스트")
    void to_string() {
        // given
        Money money = Money.of(1000L);

        // when
        String result = money.toString();

        // then
        assertThat(result).isEqualTo("1000");
    }
}