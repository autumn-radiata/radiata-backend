package radiata.service.brand.core.service.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import radiata.common.domain.brand.response.ProductGetResponseDto;
import radiata.service.brand.core.implement.RestPage;
import radiata.service.brand.core.model.entity.Product;

@Mapper(componentModel = SPRING)
public interface ProductMapper {

    @Mapping(source = "product.brand.name", target = "brand")
    @Mapping(source = "product.category.name", target = "category")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.stock.stock", target = "stock")
    ProductGetResponseDto toProductGetResponseDto(Product product);

    @Mapping(source = "content", target = "content")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "totalElements", target = "total")
    RestPage<ProductGetResponseDto> toRestPage(Page<ProductGetResponseDto> page);

    @Mapping(source = "maxDiscountAmount", target = "discountAmount")
    ProductGetResponseDto toMaxDiscountAmount(ProductGetResponseDto dto, int maxDiscountAmount);

    RestPage<ProductGetResponseDto> toRestPageFromDiscountMax(List<ProductGetResponseDto> content, int size,
        long total);

}
