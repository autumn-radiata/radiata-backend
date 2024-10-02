package radiata.service.user.core.domain.entity;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import radiata.service.user.core.domain.model.entity.PointHistory;
import radiata.service.user.core.domain.model.entity.User;
import radiata.service.user.core.domain.model.vo.Address;
import radiata.service.user.core.domain.model.vo.PointType;
import radiata.service.user.core.domain.model.vo.UserRole;


@DisplayName("유저 도메인 test")
class UserTest {

    private User user;
    private PointHistory pointHistory;


    @BeforeEach
    void setUp() {
        user = User.builder()
            .username("username")
            .password("password")
            .email("email@naver.com")
            .phone("010-3030-2003")
            .address(Address.builder()
                .detailAddress("detailAddress")
                .roadAddress("roadAddress")
                .zipcode("zipCode").build())
            .role(UserRole.CUSTOMER)
            .build();

        pointHistory = PointHistory.builder()
            .user(user)
            .rewardPoint(100)
            .pointType(PointType.SUBSCRIBE)
            .issueAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("사용자 생성")
    void testUserCreation() {

        assertThat(user.getUsername()).isEqualTo("username");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getEmail()).isEqualTo("email@naver.com");
        assertThat(user.getPhone()).isEqualTo("010-3030-2003");

        assertThat(user.getAddress().getDetailAddress()).isEqualTo("detailAddress");
        assertThat(user.getAddress().getRoadAddress()).isEqualTo("roadAddress");
        assertThat(user.getAddress().getZipcode()).isEqualTo("zipCode");

        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
    }

    @Test
    @DisplayName("사용자 정보 수정 ")
    void testUpdateInfo() {
        Address newAddress = Address.builder()
            .detailAddress("202")
            .roadAddress("456 광주")
            .zipcode("54321")
            .build();

        user.updateInfo("UpdatedUser", "010-9876-5432", newAddress);

        assertThat(user.getNickname()).isEqualTo("UpdatedUser");
        assertThat(user.getPhone()).isEqualTo("010-9876-5432");
        assertThat(user.getAddress().getRoadAddress()).isEqualTo("456 광주");
        assertThat(user.getAddress().getDetailAddress()).isEqualTo("202");
        assertThat(user.getAddress().getZipcode()).isEqualTo("54321");
    }


    @Test
    @DisplayName("적립금 내역 추가")
    void testAddPointHistory() {

        user.addPointHistory(pointHistory);

        Set<PointHistory> pointHistories = user.getPointHistories();

        assertThat(pointHistories).hasSize(1);

        PointHistory addedPointHistory = pointHistories.iterator().next();

        assertThat(addedPointHistory.getRewardPoint()).isEqualTo(100);
        assertThat(addedPointHistory.getPointType()).isEqualTo(PointType.SUBSCRIBE);
    }



}