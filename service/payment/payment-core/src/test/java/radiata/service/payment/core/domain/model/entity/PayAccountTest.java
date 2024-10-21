package radiata.service.payment.core.domain.model.entity;


import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksuid.Ksuid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import radiata.common.domain.payment.constant.Bank;
import radiata.service.payment.core.domain.model.vo.Account;

@DisplayName("Account 엔티티 테스트")
class PayAccountTest {

    PayAccount payAccount;

    @BeforeEach
    void setUp() {
        Account account = Account.of(Bank.TOSSBANK, "1234567890");
        payAccount = PayAccount.of(Ksuid.newKsuid().toString(), "주거래계좌01", account);
    }

    @Test
    @DisplayName("주거래계좌 생성")
    void account_create() {
        // then
        assertThat(payAccount.getId()).isNotBlank();
        assertThat(payAccount.getNickname()).isEqualTo("주거래계좌01");
        assertThat(payAccount.getAccount().getBank()).isEqualTo(Bank.TOSSBANK);
        assertThat(payAccount.getAccount().getAccountNumber()).isEqualTo("1234567890");
        assertThat(payAccount.getIsDefault()).isFalse();
    }

    @Test
    @DisplayName("계좌 설정")
    void account_set_default() {
        // when
        payAccount.setDefaultAccount();

        // then
        assertThat(payAccount.getIsDefault()).isTrue();
    }

    @Test
    @DisplayName("주거래계좌 해제")
    void account_unset_default() {
        // given
        payAccount.setDefaultAccount();

        // when
        payAccount.unsetDefaultAccount();

        // then
        assertThat(payAccount.getIsDefault()).isFalse();
    }

    @Test
    @DisplayName("계좌 별명 수정")
    void account_update_nickname() {
        // when
        payAccount.changeNickname("주거래계좌02");

        // then
        assertThat(payAccount.getNickname()).isEqualTo("주거래계좌02");
    }
}