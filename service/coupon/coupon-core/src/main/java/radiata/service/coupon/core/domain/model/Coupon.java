package radiata.service.coupon.core.domain.model;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import static radiata.common.message.ExceptionMessage.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import radiata.common.exception.BusinessException;
import radiata.database.model.BaseEntity;
import radiata.service.coupon.core.domain.model.constant.CouponSaleType;
import radiata.service.coupon.core.domain.model.constant.CouponType;
import radiata.service.coupon.core.domain.model.vo.CouponDiscountRate;

@Entity
@Getter
@Builder
@SQLRestriction("deleted_at IS NULL")
@Table(name = "r_coupon")
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    private String id;

    @Column(nullable = false, length = 100)
    private String title; // 쿠폰명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType couponType; // 쿠폰 타입

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponSaleType couponSaleType; // 쿠폰 할인 타입

    private Integer totalQuantity; // 쿠폰 발급 최대 수량

    private Integer issuedQuantity; // 쿠폰 현재 발급 수량

    private Integer discountAmount; // 할인 금액

    @Embedded
    private CouponDiscountRate discountRate; // 할인율

    private Integer minAvailableAmount; // 최소 사용 금액

    private Integer maxAvailableAmount; // 최대 사용 금액

    @Column(nullable = false)
    private LocalDateTime issueStartDate; // 발급 시작일

    @Column(nullable = false)
    private LocalDateTime issueEndDate; // 발급 종료일

    @OneToMany(mappedBy = "coupon", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<CouponIssue> couponIssues;

    public static Coupon of(String id, String title, CouponType couponType, CouponSaleType couponSaleType, Integer totalQuantity,
        Integer issuedQuantity, Integer discountAmount, CouponDiscountRate discountRate, Integer minAvailableAmount,
        Integer maxAvailableAmount, LocalDateTime issueStartDate, LocalDateTime issueEndDate) {

        return Coupon.builder()
            .id(id)
            .title(title)
            .couponType(couponType)
            .couponSaleType(couponSaleType)
            .totalQuantity(totalQuantity)
            .issuedQuantity(issuedQuantity)
            .discountAmount(discountAmount)
            .discountRate(discountRate)
            .minAvailableAmount(minAvailableAmount)
            .maxAvailableAmount(maxAvailableAmount)
            .issueStartDate(issueStartDate)
            .issueEndDate(issueEndDate)
            .build();
    }

    public void issue() {

        // 1. 현재 시간이 발급 가능한 시간인지 확인한다.
        if (!availableIssueDate()) {
            throw new BusinessException(COUPON_ISSUE_PERIOD);
        }
        // 2. 쿠폰 할인 타입이 선착순이면 쿠폰 발급 수량에 대한 Validation 을 수행한다.
        if (isFirstComeFirstServed()) {
            if (!availableIssuedQuantity()) {
                throw new BusinessException(COUPON_ISSUE_QUANTITY_LIMITED);
            }
        }

        issuedQuantity++;
    }

    public boolean isFirstComeFirstServed() {

        return this.couponType.isFirstComeFirstServed();
    }

    public boolean availableIssueDate() {

        LocalDateTime now = LocalDateTime.now();

        return (now.isEqual(issueStartDate) || now.isAfter(issueStartDate)) && now.isBefore(issueEndDate);
    }

    public boolean availableIssuedQuantity() {

        return issuedQuantity < totalQuantity;
    }

}
