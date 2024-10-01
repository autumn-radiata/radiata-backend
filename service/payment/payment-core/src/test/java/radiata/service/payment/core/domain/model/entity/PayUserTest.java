package radiata.service.payment.core.domain.model.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ksuid.Ksuid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.payment.core.domain.model.vo.Money;

@DisplayName("PayUser 엔티티 테스트")
class PayUserTest {

    PayUser payUser;

    @BeforeEach
    void setUp() {
        payUser = PayUser.of(Ksuid.newKsuid().toString(), "user01", "password01");
    }

    @Test
    @DisplayName("간편결제 사용자 생성")
    void pay_user_create() {
        // then
        assertThat(payUser.getId()).isNotBlank();
        assertThat(payUser.getUserId()).isEqualTo("user01");
        assertThat(payUser.getPassword()).isEqualTo("password01");
    }

    @Test
    @DisplayName("비밀번호 변경")
    void change_password() {
        // given
        String newPassword = "password02";

        // when
        payUser.changePassword(newPassword);

        // then
        assertThat(payUser.getPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("입금")
    void deposit() {
        // given
        Money money = Money.of(1000);
        Money beforeBalance = payUser.getBalance();

        // when
        payUser.deposit(money);

        // then
        assertThat(payUser.getBalance()).isEqualTo(beforeBalance.add(money));
    }

    @Test
    @DisplayName("출금 - 충전금이 충분한 경우")
    void withdraw_enough_balance() {
        // given
        Money money = Money.of(1000);
        payUser.deposit(money);

        // when
        payUser.withdraw(money);

        // then
        assertThat(payUser.getBalance()).isEqualTo(Money.ZERO);
    }

    @Test
    @DisplayName("출금 - 충전금이 부족한 경우")
    void withdraw_not_enough_balance() {
        // given
        Money money = Money.of(1000);

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> payUser.withdraw(money));

        assertThat(exception.getMessage()).isEqualTo(ExceptionMessage.INSUFFICIENT_BALANCE.getMessage());
        assertThat(exception.getHttpStatus()).isEqualTo(ExceptionMessage.INSUFFICIENT_BALANCE.getHttpStatus());
        assertThat(exception.getCode()).isEqualTo(ExceptionMessage.INSUFFICIENT_BALANCE.getCode());
    }

    @Test
    @DisplayName("결제 금액보다 충분한 충전금이 있는지 확인")
    void is_balance_less_than() {
        // given
        Money money = Money.of(1000);
        payUser.deposit(money);

        // when
        boolean result = payUser.isBalanceLessThan(Money.of(2000));

        // then
        assertThat(result).isTrue();
    }
}