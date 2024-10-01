package radiata.service.order.core.domain.model.entity;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksuid.KsuidGenerator;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import radiata.service.order.core.domain.model.constant.OrderStatus;

@DisplayName("주문 도메인 Test")
class OrderTest {

    private Order order;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {

        order = Order.create(
            KsuidGenerator.generate(),
            "userId-01",
            0,
            "Test-Address-01",
            "Test-comment-01"
            );

        orderItem = OrderItem.create(
            KsuidGenerator.generate(),
            order,
            "productId-01",
            "couponIssuedId-01",
            "rewardPointId-01",
            5,
            10000
        );
    }
    //give : 이런 값이 주어질 때
    //when : 이런 로직이 실행될 때
    //then : 이렇게 되어야 한다.

    @Test
    @DisplayName("주문 생성 Test")
    void testCreateOrder(){
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
    void testCreateOrderItem(){
        // then
        assertThat(orderItem.getId()).isNotBlank();
        assertThat(orderItem.getOrder().getId()).isEqualTo(order.getId());
        assertThat(orderItem.getProductId()).isEqualTo("productId-01");
        assertThat(orderItem.getCouponIssuedId()).isEqualTo("couponIssuedId-01");
        assertThat(orderItem.getRewardPointId()).isEqualTo("rewardPointId-01");
        assertThat(orderItem.getQuantity()).isEqualTo(5);
        assertThat(orderItem.getUnitPrice()).isEqualTo(10000);
        assertThat(orderItem.getPaymentPrice()).isEqualTo(50000);
    }

    @Test
    @DisplayName("주문 엔티티 - 상품목록 지정 Test")
    void testSetOrderItems(){
        Set<OrderItem> orderItems = new HashSet<>();
        // given
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);

        // then
        for (OrderItem orderItem2 : order.getItemList()) {
            assertThat(orderItem2.getId()).isEqualTo(orderItem.getId());
            assertThat(orderItem2.getProductId()).isEqualTo(orderItem.getProductId());
            assertThat(orderItem2.getCouponIssuedId()).isEqualTo(orderItem.getCouponIssuedId());
            assertThat(orderItem2.getRewardPointId()).isEqualTo(orderItem.getRewardPointId());
            assertThat(orderItem2.getQuantity()).isEqualTo(orderItem.getQuantity());
            assertThat(orderItem2.getUnitPrice()).isEqualTo(orderItem.getUnitPrice());
            assertThat(orderItem2.getPaymentPrice()).isEqualTo(orderItem.getPaymentPrice());
        }
    }

    @Test
    @DisplayName("주문 상태 변경 Test")
    void updateOrderStatus(){
        // given
        order.updateOrderStatus(OrderStatus.PAYMENT_PENDING);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAYMENT_PENDING);
    }
}