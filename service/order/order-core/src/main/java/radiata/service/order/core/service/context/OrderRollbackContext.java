package radiata.service.order.core.service.context;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class OrderRollbackContext {

    // 롤백에 필요한 리스트
    private List<String> deductedTimeSales = new ArrayList<>();
    private List<String> deductedProducts = new ArrayList<>();
    private List<String> usedCoupons = new ArrayList<>();

    public void addDeductedTimeSale(String timeSaleProductId) {
        deductedTimeSales.add(timeSaleProductId);
    }

    public void addDeductedProduct(String productId) {
        deductedProducts.add(productId);
    }

    public void addUsedCoupon(String couponIssuedId) {
        usedCoupons.add(couponIssuedId);
    }
}
