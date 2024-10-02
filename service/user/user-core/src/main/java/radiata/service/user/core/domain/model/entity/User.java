package radiata.service.user.core.domain.model.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import radiata.service.user.core.domain.model.vo.Address;
import radiata.service.user.core.domain.model.vo.Point;
import radiata.service.user.core.domain.model.constant.UserRole;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder
@Table(name = "r_user")
@SQLRestriction("deleted_at IS NULL")
public class User{

    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Column
    private String nickname;

    @Column
    private String phone;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @Embedded
    private Point totalPoint;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;


    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private Set<PointHistory> pointHistories = new HashSet<>();

    /**
     * 사용자 생성
     */
    public static User of(String username, String password, String email, String nickname,
        String phone, Address address,UserRole role) {
        return User.builder()
            .username(username)
            .password(password)
            .email(email)
            .nickname(nickname)
            .phone(phone)
            .address(address)
            .totalPoint(Point.builder().point(0).build())
            .role(role)
            .build();

    }

    /**
     * 사용자 정보 수정
     */
    public void updateInfo(String nickname, String phone, Address address) {
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
    }

    /**
     * 적립금 내역 추가
     */
    public void addPointHistory(PointHistory history) {
        this.pointHistories.add(history);
    }


}

