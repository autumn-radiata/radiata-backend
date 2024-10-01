package radiata.service.payment.core.domain.model.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksuid.Ksuid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import radiata.service.payment.core.domain.model.vo.Money;

@DisplayName("MoneyChangeHistory 엔티티 테스트")
class MoneyChangeHistoryTest {

    MoneyChangeHistory moneyChangeHistory;

    @BeforeEach
    void setUp() {
        PayUser payUser = PayUser.of(Ksuid.newKsuid().toString(), "user01", "password01");
        moneyChangeHistory = MoneyChangeHistory.of(Ksuid.newKsuid().toString(), Money.of(1000), payUser);
    }

    @Test
    @DisplayName("MoneyChangeHistory 생성")
    void money_change_history_create() {
        // then
        assertThat(moneyChangeHistory.getId()).isNotBlank();
        assertThat(moneyChangeHistory.getAmount()).isEqualTo(Money.of(1000));
        assertThat(moneyChangeHistory.getPayUser().getUserId()).isEqualTo("user01");
    }
}