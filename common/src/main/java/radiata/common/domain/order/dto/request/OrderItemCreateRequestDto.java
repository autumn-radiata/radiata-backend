package radiata.common.domain.order.dto.request;

public record OrderItemCreateRequestDto(
    String productId,
    String timesaleProductId,
    String couponIssuedId,
    String rewardPointId,
    Integer quantity) {

}
