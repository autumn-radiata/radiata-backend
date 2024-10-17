package radiata.service.payment.core.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import radiata.database.model.BaseEntity;
import radiata.service.payment.core.domain.model.vo.Account;

@Getter
@Entity
@Table(name = "r_pay_account")
@SQLRestriction("deleted_at IS NULL")
@Builder // 빌더 패턴은 테스트 용도로만 사용
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 객체는 기본 생성자를 필요로 함
public class PayAccount extends BaseEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String nickname;

    @Embedded
    private Account account;

    @Column(nullable = false)
    private Boolean isDefault;

    public static PayAccount of(
        String id,
        String nickname,
        Account account
    ) {
        return PayAccount.builder()
            .id(id)
            .nickname(nickname)
            .account(account)
            .isDefault(false)
            .build();
    }

    /**
     * 주거래 계좌 등록
     */
    public void setDefaultAccount() {
        this.isDefault = true;
    }

    /**
     * 주거래 계좌 해제
     */
    public void unsetDefaultAccount() {
        this.isDefault = false;
    }

    /**
     * 계좌 별명 변경
     */
    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }
}
