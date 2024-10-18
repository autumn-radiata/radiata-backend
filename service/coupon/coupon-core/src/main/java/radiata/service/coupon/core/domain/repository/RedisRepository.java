package radiata.service.coupon.core.domain.repository;

public interface RedisRepository {

    Long sAdd(String key, String value);

    Long sCard(String key);

    Boolean sIsMember(String key, String value);

    void issueRequest(String couponId, String userId, Integer totalIssueQuantity);
}
