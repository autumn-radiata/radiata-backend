package radiata.common.domain.coupon.dto.condition;

public record CouponSearchCondition(
    String couponId, // 쿠폰 아이디
    String title, // 쿠폰명
    String couponType, // 쿠폰 타입 (FIRST_COME_FIRST_SERVED, UNLIMITED)
    String couponSaleType // 쿠폰 세일 타입 (RATE, AMOUNT)
) {

}
