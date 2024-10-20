package radiata.common.domain.brand.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductModifyRequestDto(

    @NotBlank(message = "상품 식별자는 필수 입니다.")
    String productId,

    @NotBlank(message = "브랜드 식별자는 필수 입니다.")
    String brandId,

    @NotBlank(message = "카테고리 식별자는 필수 입니다.")
    String categoryId,

    @NotBlank(message = "상품명은 필수 입니다.")
    @Size(max = 50, message = "상품명은 최대 50자입니다.")
    String name,

    @Positive(message = "상품 가격은 0원보다 커야합니다.")
    Integer price,

    @PositiveOrZero(message = "상품 할인가격은 0원이상이여야 합니다.")
    Integer discountAmount,

    @PositiveOrZero(message = "상품의 재고는 0개 이상이여야 합니다.")
    Integer stock,

    @NotBlank(message = "상품 성별은 필수입니다.")
    @Pattern(regexp = "^(ALL|WOMAN|MAN)$", message = "성별은 all, woman, man 중 하나여야 합니다.")
    String gender,

    @NotBlank(message = "상품 색상은 필수입니다.")
    @Pattern(regexp = "^(RED|ORANGEYELLOW|GREEN|BLUE|NAVY|PURPLE)$", message = "정해진 색상으로 등록해주세요.")
    String color,

    @NotBlank(message = "상품 사이즈는 필수입니다.")
    @Pattern(regexp = "^(SMALL|MEDIUM|LARGE|FREE)$", message = "사이즈는 S,M,L,F 중 하나여야 합니다.")
    String size

) {

}
