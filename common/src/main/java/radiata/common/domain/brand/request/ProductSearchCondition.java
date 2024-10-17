package radiata.common.domain.brand.request;

import org.springframework.web.bind.annotation.RequestParam;

public record ProductSearchCondition(
    @RequestParam(required = false)
    String brandId,
    @RequestParam(required = false)
    String categoryId,
    @RequestParam(required = false)
    String gender,
    @RequestParam(required = false)
    String size,
    @RequestParam(required = false)
    String color,
    @RequestParam(required = false)
    String query
) {


}
