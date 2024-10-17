package radiata.service.order.core.domain.model.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksuid.KsuidGenerator;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import radiata.service.order.core.domain.model.constant.OrderStatus;

@DisplayName("주문 엔티티 Test")
class OrderTest {

    private Order order;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {

        order = Order.of(
            KsuidGenerator.generate(),
            "userId-01",
            "Test-Address-01",
            "Test-comment-01"
        );

        orderItem = OrderItem.of(
            KsuidGenerator.generate(),
            order,
            "productId-01",
            "timeSaleId-01",
            "couponIssuedId-01",
            5,
            10000
        );
    }


    @Test
    @DisplayName("주문 생성 Test")
    void testCreateOrder() {
        // then
        assertThat(order.getId()).isNotBlank();
        assertThat(order.getUserId()).isEqualTo("userId-01");
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAYMENT_REQUESTED);
        assertThat(order.getOrderPrice()).isEqualTo(0);
        assertThat(order.getIsRefunded()).isFalse();
        assertThat(order.getPaymentId()).isBlank();
        assertThat(order.getAddress()).isEqualTo("Test-Address-01");
        assertThat(order.getComment()).isEqualTo("Test-comment-01");
    }

    @Test
    @DisplayName("주문 상품 생성 Test")
    void testCreateOrderItem() {
        // then
        assertThat(orderItem.getId()).isNotBlank();
        assertThat(orderItem.getOrder().getId()).isEqualTo(order.getId());
        assertThat(orderItem.getProductId()).isEqualTo("productId-01");
        assertThat(orderItem.getTimeSaleProductId()).isEqualTo("timeSaleId-01");
        assertThat(orderItem.getCouponIssuedId()).isEqualTo("couponIssuedId-01");
        assertThat(orderItem.getQuantity()).isEqualTo(5);
        assertThat(orderItem.getUnitPrice()).isEqualTo(10000);
    }

    @Test
    @DisplayName("주문 - 총 금액 & 상품 목록 지정 Test")
    void testSetOrderItems() {
        // given
        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(orderItem);

        // when
        order.setOrderPriceAndItems(0, orderItems);

        // then
        for (OrderItem orderItem2 : order.getOrderItems()) {
            assertThat(orderItem2.getId()).isEqualTo(orderItem.getId());
            assertThat(orderItem2.getProductId()).isEqualTo(orderItem.getProductId());
            assertThat(orderItem2.getCouponIssuedId()).isEqualTo(orderItem.getCouponIssuedId());
            assertThat(orderItem2.getQuantity()).isEqualTo(orderItem.getQuantity());
            assertThat(orderItem2.getUnitPrice()).isEqualTo(orderItem.getUnitPrice());
            assertThat(orderItem2.getPaymentPrice()).isEqualTo(orderItem.getPaymentPrice());
        }
    }

    @Test
    @DisplayName("주문 상태 변경 Test")
    void updateOrderStatus() {
        // when
        order.updateOrderStatus(OrderStatus.PAYMENT_PENDING);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAYMENT_PENDING);
    }
}