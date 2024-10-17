package radiata.service.order.core.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import radiata.common.domain.brand.request.ProductDeductRequestDto;
import radiata.common.domain.coupon.dto.response.CouponResponseDto;
import radiata.common.domain.order.dto.response.CouponInfoDto;
import radiata.service.order.core.service.client.CouponIssueClient;
import radiata.service.order.core.service.client.ProductClient;
import radiata.service.order.core.service.client.TimeSaleProductClient;

@Service
@RequiredArgsConstructor
public class ProcessService {

    private final TimeSaleProductClient timeSaleProductClient;
    private final ProductClient productClient;
    private final CouponIssueClient couponIssueClient;


    // 타임세일상품 재고 확인 및 차감 요청
    public void checkAndDeductTimeSaleProduct(List<String> deductedTimeSales, String timeSaleProductId) {
        if (timeSaleProductId != null) {
            timeSaleProductClient.checkAndDeductTimeSaleProduct(timeSaleProductId);
            deductedTimeSales.add(timeSaleProductId);
        }
    }

    // 상품 재고 확인 및 차감 요청
    public void checkAndDeductStock(List<String> deductedProducts, String productId, int quantity) {
        productClient.checkAndDeductStock(new ProductDeductRequestDto(productId, quantity));
        deductedProducts.add(productId);
    }

    // 쿠폰 확인 및 상태변경 요청
    public CouponInfoDto checkAndUseCoupon(List<String> usedCoupons, String couponIssuedId, String userId) {
        String saleType = null;
        Integer discountValue = null;
        if (couponIssuedId != null) {
            String couponId = couponIssueClient.useCouponIssue(couponIssuedId, userId).data().couponId();
            usedCoupons.add(couponIssuedId);

            CouponResponseDto couponInfo = couponIssueClient.getCouponType(couponId).data();
            saleType = couponInfo.couponSaleType();
            discountValue = checkCouponSaleType(couponInfo);
        }
        return CouponInfoDto.of(saleType, discountValue);
    }

    private int checkCouponSaleType(CouponResponseDto couponInfo) {
        // TODO - CouponType enum 으로 수정
        if (couponInfo.couponSaleType().equals("AMOUNT")) {
            return couponInfo.discountAmount();
        }
        return couponInfo.discountRate();
    }
}
