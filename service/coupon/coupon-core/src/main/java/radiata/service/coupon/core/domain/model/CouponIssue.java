package radiata.service.coupon.core.domain.model;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import static radiata.common.domain.coupon.constant.CouponStatus.EXPIRED;
import static radiata.common.domain.coupon.constant.CouponStatus.ISSUED;
import static radiata.common.domain.coupon.constant.CouponStatus.USED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.common.domain.coupon.constant.CouponStatus;

@Entity
@Getter
@Builder
@SQLRestriction("deleted_at IS NULL")
@Table(name = "r_coupon_issue")
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class CouponIssue extends BaseEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(nullable = false)
    private String userId;

    @Enumerated(STRING)
    private CouponStatus couponStatus;

    @CreatedDate
    private LocalDateTime issuedAt;

    private LocalDateTime usedAt;

    private LocalDateTime expiredAt;

    public static CouponIssue from(Coupon coupon, String userId, String couponIssueId) {

        return CouponIssue.builder()
            .id(couponIssueId)
            .coupon(coupon)
            .userId(userId)
            .couponStatus(ISSUED)
            .expiredAt(coupon.getIssueEndDate())
            .build();
    }

    public void use(String userId) {

        if (!this.userId.equals(userId)) {
            throw new BusinessException(ExceptionMessage.COUPON_CAN_NOT_USE);
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(expiredAt) || couponStatus.equals(EXPIRED) || couponStatus.equals(USED)) {
            throw new BusinessException(ExceptionMessage.COUPON_CAN_NOT_USE);
        }

        if (usedAt != null && usedAt.isBefore(now)) {
            throw new BusinessException(ExceptionMessage.COUPON_CAN_NOT_USE);
        }

        this.usedAt = now;
        this.couponStatus = USED;
    }


    public void rollback(CouponStatus couponStatus) {

        this.usedAt = null;
        this.couponStatus = couponStatus;
    }
}
