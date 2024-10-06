package radiata.service.user.core.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import radiata.service.user.core.domain.model.entity.User;
import radiata.service.user.core.domain.model.vo.Address;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// @TestPropertySource(locations = "classpath:application-test.yml") // 필요에 따라 주석 해제
class UserRepositoryTest {

   /* @Autowired
    private UserJpaRepository userJpaRepository; // UserJpaRepository를 주입합니다.

    @BeforeEach
    void setUp() {
        Address address = Address.builder()
            .detailAddress("detailAddress")
            .roadAddress("roadAddress")
            .zipcode("zipcode")
            .build();

        User user = User.builder()
            .userId("userId")
            .email("email@naver.com")
            .address(address)
            .build();

        userJpaRepository
            .save(user); // UserJpaRepository를 통해 user를 저장합니다.
    }

    @Test
    void test() {
        boolean result = userJpaRepository.existsByEmail("email@naver.com");
        assertThat(result).isTrue();
    }*/
}
