package radiata.common.domain.timesale.dto.request;

import org.hibernate.validator.constraints.Range;

public record TimeSaleProductSaleRequestDto(

        @Range(min = 1, message = "수량은 최소 1 부터 입력할 수 있습니다.")
        Integer quantity
) {

}
