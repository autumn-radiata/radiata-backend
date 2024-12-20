package radiata.common.domain.coupon.constant;

public enum CouponType {
    FIRST_COME_FIRST_SERVED, UNLIMITED;

    public boolean isFirstComeFirstServed() {
        return this.equals(FIRST_COME_FIRST_SERVED);
    }

    public boolean isUnlimited() {
        return this.equals(UNLIMITED);
    }
}
