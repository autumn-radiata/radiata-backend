package radiata.service.coupon.core.util;

public class RedisKeyUtil {

    public static final String COUPON_ISSUE_REQUEST_REDIS_KEY_PREFIX = "couponIssueRequest:";

    public static String getIssueRequestKey(String couponId) {

        return COUPON_ISSUE_REQUEST_REDIS_KEY_PREFIX + couponId;
    }
}
