package radiata.service.timesale.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;

class TimeSaleProductTest {

    @Nested
    @DisplayName("타임세일 상품 판매량 증가 테스트")
    class SaleTest {

        @Test
        @DisplayName("타임세일 상품에 문제가 없을 경우 판매량이 올라간다.")
        void sale_1() {
            // Given
            String productId = "productId";
            TimeSaleProduct timeSaleProduct = TimeSaleProduct.builder()
                .productId(productId)
                .timeSaleStartTime(LocalDateTime.now().minusDays(1))
                .timeSaleEndTime(LocalDateTime.now().plusDays(1))
                .saleQuantity(0)
                .totalQuantity(100)
                .build();
            // When
            timeSaleProduct.sale();
            // Then
            assertThat(timeSaleProduct.getSaleQuantity()).isEqualTo(1);
        }

        @Test
        @DisplayName("타임세일 기간이 지난 경우 예외를 반환한다.")
        void sale_2() {
            // Given
            String productId = "productId";
            TimeSaleProduct timeSaleProduct = TimeSaleProduct.builder()
                .productId(productId)
                .timeSaleStartTime(LocalDateTime.now().minusDays(2))
                .timeSaleEndTime(LocalDateTime.now().minusDays(1))
                .saleQuantity(0)
                .totalQuantity(100)
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(timeSaleProduct::sale,
                BusinessException.class);
            assertThat(result).hasMessage(ExceptionMessage.TIME_SALE_PRODUCT_PERIOD.getMessage());
        }

        @Test
        @DisplayName("타임세일 기간이 되지 않은 경우 예외를 반환한다.")
        void sale_3() {
            // Given
            String productId = "productId";
            TimeSaleProduct timeSaleProduct = TimeSaleProduct.builder()
                .productId(productId)
                .timeSaleStartTime(LocalDateTime.now().plusDays(1))
                .timeSaleEndTime(LocalDateTime.now().plusDays(2))
                .saleQuantity(0)
                .totalQuantity(100)
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(timeSaleProduct::sale,
                BusinessException.class);
            assertThat(result).hasMessage(ExceptionMessage.TIME_SALE_PRODUCT_PERIOD.getMessage());
        }


        @Test
        @DisplayName("타임세일 상품 재고가 떨어진 경우 예외를 반환한다.")
        void sale_4() {
            // Given
            String productId = "productId";
            TimeSaleProduct timeSaleProduct = TimeSaleProduct.builder()
                .productId(productId)
                .timeSaleStartTime(LocalDateTime.now().minusDays(1))
                .timeSaleEndTime(LocalDateTime.now().plusDays(1))
                .saleQuantity(100)
                .totalQuantity(100)
                .build();
            // When + Then
            BusinessException result = catchThrowableOfType(timeSaleProduct::sale,
                BusinessException.class);
            assertThat(result).hasMessage(ExceptionMessage.TIME_SALE_PRODUCT_LIMITED_SALE.getMessage());
        }
    }

}