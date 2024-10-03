package radiata.service.coupon.core.domain.model.constant;

public enum CouponType {
    FIRST_COME_FIRST_SERVED, UNLIMITED;

    public boolean isFirstComeFirstServed() {
        return this.equals(FIRST_COME_FIRST_SERVED);
    }
}
