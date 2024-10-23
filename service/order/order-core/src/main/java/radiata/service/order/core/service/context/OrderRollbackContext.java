package radiata.service.order.core.service.context;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import radiata.common.domain.order.dto.response.StockInfoDto;

@Getter
public class OrderRollbackContext {

    // 롤백에 필요한 리스트
    private List<StockInfoDto> deductedTimeSales = new ArrayList<>();
    private List<StockInfoDto> deductedProducts = new ArrayList<>();
    private List<String> usedCoupons = new ArrayList<>();

    public void addDeductedTimeSale(String timeSaleProductId, int quantity) {
        deductedTimeSales.add(new StockInfoDto(timeSaleProductId, quantity));
    }

    public void addDeductedProduct(String productId, int quantity) {
        deductedProducts.add(new StockInfoDto(productId, quantity));
    }

    public void addUsedCoupon(String couponIssuedId) {
        usedCoupons.add(couponIssuedId);
    }
}
