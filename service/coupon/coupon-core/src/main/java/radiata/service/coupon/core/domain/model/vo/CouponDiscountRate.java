package radiata.service.coupon.core.domain.model.vo;

import static radiata.common.message.ExceptionMessage.INVALID_INPUT_COUPON_DISCOUNT_RATE;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import radiata.common.exception.BusinessException;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponDiscountRate {

    @Column(name = "discount_rate")
    private Integer value;

    public CouponDiscountRate(Integer value) {

        if (value == null || value < 1 || value > 100) {
            throw new BusinessException(INVALID_INPUT_COUPON_DISCOUNT_RATE);
        }

        this.value = value;
    }

    @Override
    public String toString() {

        return value.toString();
    }
}
