package radiata.service.payment.core.domain.model.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import radiata.common.domain.payment.constant.Bank;

@DisplayName("Account VO 테스트")
class AccountTest {

    @Test
    @DisplayName("Account VO 생성")
    void createAccount() {
        // given
        Bank bank = Bank.CITI;
        String accountNumber = "1234567890";

        // when
        Account account = Account.of(bank, accountNumber);

        // then
        assertThat(account.getBank()).isEqualTo(bank);
        assertThat(account.getAccountNumber()).isEqualTo(accountNumber);
    }

    @Test
    @DisplayName("Account VO 문자열 변환")
    void convertToString() {
        // given
        Bank bank = Bank.CITI;
        String accountNumber = "1234567890";
        Account account = Account.of(bank, accountNumber);

        // when
        String accountString = account.toString();

        // then
        assertThat(accountString).isEqualTo(bank.getName() + " " + accountNumber);
    }
}