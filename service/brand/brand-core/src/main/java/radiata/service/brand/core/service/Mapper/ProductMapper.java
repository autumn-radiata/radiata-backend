package radiata.service.brand.core.service.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import radiata.common.domain.brand.response.ProductGetResponseDto;
import radiata.service.brand.core.model.entity.Product;

@Mapper(componentModel = SPRING)
public interface ProductMapper {

    @Mapping(source = "product.brand.name", target = "brand")
    @Mapping(source = "product.category.name", target = "category")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.stock.stock", target = "stock")
    ProductGetResponseDto toProductGetResponseDto(Product product);
}
