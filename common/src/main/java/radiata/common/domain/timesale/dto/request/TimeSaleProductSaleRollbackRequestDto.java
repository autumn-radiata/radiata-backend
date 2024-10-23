package radiata.common.domain.timesale.dto.request;

public record TimeSaleProductSaleRollbackRequestDto(
        String timeSaleProductId,
        Integer quantity
) {

}
