package radiata.service.coupon.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.coupon.core.CouponCoreConfiguration;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.model.CouponIssue;
import radiata.service.coupon.core.domain.model.constant.CouponSaleType;
import radiata.service.coupon.core.domain.model.constant.CouponStatus;
import radiata.service.coupon.core.domain.model.constant.CouponType;
import radiata.service.coupon.core.domain.repository.CouponIssueQueryRepository;
import radiata.service.coupon.core.domain.repository.CouponIssueRepository;
import radiata.service.coupon.core.domain.repository.CouponRepository;
import radiata.service.coupon.core.infrastructure.repository.CouponIssueJpaRepository;
import radiata.service.coupon.core.infrastructure.repository.CouponJpaRepository;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(classes = CouponCoreConfiguration.class)
class CouponIssueServiceImplV1Test {

    @Autowired
    CouponIssueServiceImplV1 couponIssueService;

    @Autowired
    CouponIssueJpaRepository couponIssueJpaRepository;

    @Autowired
    CouponIssueRepository couponIssueRepository;

    @Autowired
    CouponIssueQueryRepository couponIssueQueryRepository;

    @Autowired
    CouponJpaRepository couponJpaRepository;

    @Autowired
    CouponRepository couponRepository;

    @MockBean
    private RedissonClient redissonClient;

    @MockBean
    @Qualifier("redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @MockBean
    @Qualifier("springRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @MockBean
    private RedisConnectionFactory redisConnectionFactory;

    @MockBean
    private RedisKeyValueAdapter redisKeyValueAdapter;

    @BeforeEach
    void clean() {
        couponJpaRepository.deleteAllInBatch();
        couponIssueJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("쿠폰 발급 내역이 존재하면 예외를 반환한다.")
    void saveCouponIssue_1() {
        // Given
        Coupon coupon = Coupon.builder()
            .id("id")
            .couponType(CouponType.FIRST_COME_FIRST_SERVED)
            .couponSaleType(CouponSaleType.AMOUNT)
            .title("선착순 테스트 쿠폰")
            .totalQuantity(100)
            .issuedQuantity(100)
            .issueStartDate(LocalDateTime.now().minusDays(1))
            .issueEndDate(LocalDateTime.now().plusDays(1))
            .build();
        couponRepository.save(coupon);

        CouponIssue couponIssue = CouponIssue.builder()
            .id("id1")
            .coupon(coupon)
            .userId("userId")
            .couponStatus(CouponStatus.ISSUED)
            .issuedAt(LocalDateTime.now())
            .build();
        couponIssueRepository.save(couponIssue);
        // When + Then
        BusinessException businessException = catchThrowableOfType(
            () -> couponIssueService.saveCouponIssue(couponIssue.getCoupon().getId(), couponIssue.getUserId()),
            BusinessException.class);
        assertThat(businessException.getMessage()).isEqualTo(
            ExceptionMessage.DUPLICATED_COUPON_ISSUE.getMessage());

    }

    @Test
    @DisplayName("쿠폰 발급 내역이 존재하지 않는다면 쿠폰을 발급한다.")
    void saveCouponIssue_2() {
        // Given
        String userId = "1";
        Coupon coupon = Coupon.builder()
            .id("id")
            .couponType(CouponType.FIRST_COME_FIRST_SERVED)
            .couponSaleType(CouponSaleType.AMOUNT)
            .title("선착순 테스트 쿠폰")
            .totalQuantity(100)
            .issuedQuantity(100)
            .issueStartDate(LocalDateTime.now().minusDays(1))
            .issueEndDate(LocalDateTime.now().plusDays(1))
            .build();
        couponRepository.save(coupon);
        // When
        couponIssueService.saveCouponIssue(coupon.getId(), userId);
        // Then
        assertThat(couponIssueJpaRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("발급 수량, 기한, 중복 발급 문제가 없다면 쿠폰을 발급한다.")
    void issue_1() {
        // Given
        String userId = "userId";
        Coupon coupon = Coupon.builder()
            .id("id")
            .couponType(CouponType.FIRST_COME_FIRST_SERVED)
            .couponSaleType(CouponSaleType.AMOUNT)
            .title("선착순 테스트 쿠폰")
            .totalQuantity(100)
            .issuedQuantity(0)
            .issueStartDate(LocalDateTime.now().minusDays(1))
            .issueEndDate(LocalDateTime.now().plusDays(1))
            .build();
        couponRepository.save(coupon);
        // When
        couponIssueService.issue(coupon.getId(), userId);
        // Then
        Coupon couponResult = couponRepository.findById(coupon.getId()).get();
        assertThat(couponResult.getIssuedQuantity()).isEqualTo(1);

        CouponIssue result = couponIssueQueryRepository.findFirstCouponIssue(coupon.getId(), userId);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("발급 수량에 문제가 있다면 예외를 반환한다.")
    void issue_2() {
        // Given
        String userId = "userId";
        Coupon coupon = Coupon.builder()
            .id("id")
            .couponType(CouponType.FIRST_COME_FIRST_SERVED)
            .couponSaleType(CouponSaleType.AMOUNT)
            .title("선착순 테스트 쿠폰")
            .totalQuantity(100)
            .issuedQuantity(100)
            .issueStartDate(LocalDateTime.now().minusDays(1))
            .issueEndDate(LocalDateTime.now().plusDays(1))
            .build();
        couponRepository.save(coupon);
        // When & Then
        BusinessException result = catchThrowableOfType(
            () -> couponIssueService.issue(coupon.getId(), userId), BusinessException.class);
        assertThat(result.getMessage()).isEqualTo(ExceptionMessage.COUPON_ISSUE_QUANTITY_LIMITED.getMessage());
    }

    @Test
    @DisplayName("발급 기한에 문제가 있다면 예외를 반환한다.")
    void issue_3() {
        // Given
        String userId = "userId";
        Coupon coupon = Coupon.builder()
            .id("id")
            .couponType(CouponType.FIRST_COME_FIRST_SERVED)
            .couponSaleType(CouponSaleType.AMOUNT)
            .title("선착순 테스트 쿠폰")
            .totalQuantity(100)
            .issuedQuantity(0)
            .issueStartDate(LocalDateTime.now().plusDays(1))
            .issueEndDate(LocalDateTime.now().plusDays(2))
            .build();
        couponRepository.save(coupon);
        // When & Then
        BusinessException result = catchThrowableOfType(
            () -> couponIssueService.issue(coupon.getId(), userId), BusinessException.class);
        assertThat(result.getMessage()).isEqualTo(ExceptionMessage.COUPON_ISSUE_PERIOD.getMessage());
    }

    @Test
    @DisplayName("중복 발급 검증에 문제가 있다면 예외를 반환한다.")
    void issue_4() {
        // Given
        String userId = "userId";
        Coupon coupon = Coupon.builder()
            .id("id")
            .couponType(CouponType.FIRST_COME_FIRST_SERVED)
            .couponSaleType(CouponSaleType.AMOUNT)
            .title("선착순 테스트 쿠폰")
            .totalQuantity(100)
            .issuedQuantity(0)
            .issueStartDate(LocalDateTime.now().minusDays(1))
            .issueEndDate(LocalDateTime.now().plusDays(1))
            .build();
        couponRepository.save(coupon);

        CouponIssue couponIssue = CouponIssue.builder()
            .id("id1")
            .coupon(coupon)
            .userId(userId)
            .couponStatus(CouponStatus.ISSUED)
            .issuedAt(LocalDateTime.now())
            .build();
        couponIssueRepository.save(couponIssue);

        // When & Then
        BusinessException result = catchThrowableOfType(
            () -> couponIssueService.issue(coupon.getId(), userId), BusinessException.class);
        assertThat(result.getMessage()).isEqualTo(ExceptionMessage.DUPLICATED_COUPON_ISSUE.getMessage());
    }

    @Test
    @DisplayName("쿠폰이 존재하지 않는다면 예외를 반환한다.")
    void issue_5() {
        // Given
        String couponId = "couponId";
        String userId = "userId";

        // When & Then
        BusinessException result = catchThrowableOfType(
            () -> couponIssueService.issue(couponId, userId), BusinessException.class);
        assertThat(result.getMessage()).isEqualTo(ExceptionMessage.NOT_FOUND.getMessage());
    }

}