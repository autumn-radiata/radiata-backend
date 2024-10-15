package radiata.common.domain.brand.response;

import java.io.Serializable;

public record ProductGetResponseDto(
    String productId,
    String brand,
    String category,
    String name,
    Integer price,
    Integer discountAmount,
    Integer stock,
    String gender,
    String color,
    String size) implements Serializable {

}
