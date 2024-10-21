package radiata.service.payment.core.domain.model.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksuid.Ksuid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import radiata.common.domain.payment.constant.Bank;
import radiata.common.domain.payment.constant.FirmBankingRequestStatus;
import radiata.service.payment.core.domain.model.vo.Account;
import radiata.service.payment.core.domain.model.vo.Money;

@DisplayName("FirmBankingRequestHistory 엔티티 테스트")
class FirmBankingRequestHistoryTest {

    FirmBankingRequestHistory firmBankingRequestHistory;

    @BeforeEach
    void setUp() {
        firmBankingRequestHistory = FirmBankingRequestHistory.of(
            Ksuid.newKsuid().toString(),
            Money.of(1000L),
            FirmBankingRequestStatus.SUCCESS,
            Account.of(Bank.CITI, "1234567890"),
            Account.of(Bank.BUSANBANK, "0987654321")
        );
    }

    @Test
    @DisplayName("FirmBankingRequestHistory 엔티티 생성")
    void createFirmBankingRequestHistory() {
        // then
        assertThat(firmBankingRequestHistory.getId()).isNotBlank();
        assertThat(firmBankingRequestHistory.getAmount()).isEqualTo(Money.of(1000L));
        assertThat(firmBankingRequestHistory.getStatus()).isEqualTo(FirmBankingRequestStatus.SUCCESS);
        assertThat(firmBankingRequestHistory.getFromAccount().getBank()).isEqualTo(Bank.CITI);
        assertThat(firmBankingRequestHistory.getFromAccount().getAccountNumber()).isEqualTo("1234567890");
        assertThat(firmBankingRequestHistory.getToAccount().getBank()).isEqualTo(Bank.BUSANBANK);
        assertThat(firmBankingRequestHistory.getToAccount().getAccountNumber()).isEqualTo("0987654321");
    }
}