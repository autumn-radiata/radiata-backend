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
import radiata.database.model.BaseEntity;
import radiata.service.user.core.domain.model.constant.UserRole;
import radiata.service.user.core.domain.model.vo.Address;
import radiata.service.user.core.domain.model.vo.Point;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder
@Table(name = "r_user")
@SQLRestriction("deleted_at IS NULL")
public class User extends BaseEntity {

    @Id
    private String id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

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

    public static User of(String id, String password, String email, String nickname, String phone,
        String roadAddress, String detailAddress, String zipcode, UserRole role) {
        Address address = Address.of(roadAddress, detailAddress, zipcode);
        return User.builder()
            .id(id)
            .password(password)
            .email(email)
            .nickname(nickname)
            .phone(phone)
            .address(address)
            .totalPoint(Point.from(0))
            .role(role)
            .build();
    }

    public void updateInfo(String nickname, String phone, String roadAddress, String detailAddress, String zipcode) {
        Address address = Address.of(roadAddress, detailAddress, zipcode);
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
    }

    //todo : 수정에 맞게 다시 작성
    public void delete(String userId) {
        deleteEntity();
    }

    public void addPointHistory(PointHistory history) {
        this.pointHistories.add(history);
    }


}

