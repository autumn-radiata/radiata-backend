package radiata.service.payment.core.domain.model.vo;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Bank VO 테스트")
class BankTest {

    @Test
    @DisplayName("은행코드로 은행 조회")
    void find_bank_by_code() {
        // given
        String code = "004";

        // when
        Optional<Bank> optionalBank = Bank.findByCode(code);

        // then
        assertThat(optionalBank).isPresent();
        assertThat(optionalBank.get()).isEqualTo(Bank.KOOKMIN);
    }

    @Test
    @DisplayName("은행코드로 은행 조회 - 존재하지 않는 코드")
    void find_bank_by_code_not_exist() {
        // given
        String code = "999";

        // when
        Optional<Bank> optionalBank = Bank.findByCode(code);

        // then
        assertThat(optionalBank).isEmpty();
    }
}