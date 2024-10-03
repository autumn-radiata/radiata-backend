package radiata.service.order.core.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import radiata.common.domain.order.dto.request.OrderCreateRequestDto;
import radiata.common.domain.order.dto.request.OrderItemCreateRequestDto;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.service.order.core.OrderCoreConfiguration;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.service.OrderService;

@SpringBootTest(classes = OrderCoreConfiguration.class)
@DisplayName("주문 서비스 Test")
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    OrderCreateRequestDto orderCreateRequestDto;
    OrderResponseDto createdOrder; // createdOrder를 클래스 필드로 정의

    @BeforeEach
    void setUp() {
        // given
        List<OrderItemCreateRequestDto> itemList = new ArrayList<>();
        String productId = "product-0";
        String timesaleProductId = "timesaleProduct-0";
        String couponIssuedId = "couponIssued-0";
        String rewardPointId = "rewardPoint-0";

        for (int i = 1; i <= 3; i++) {
            OrderItemCreateRequestDto itemListRequestDto = new OrderItemCreateRequestDto(
                productId + i,
                timesaleProductId + i,
                couponIssuedId + i,
                rewardPointId + i,
                10
            );

            itemList.add(itemListRequestDto);
        }

        orderCreateRequestDto = new OrderCreateRequestDto(
            "address-01",
            "comment-01",
            itemList
        );

        // 주문 생성
        createdOrder = orderService.createOrder(orderCreateRequestDto, "user-01");
    }

    @Test
    @DisplayName("주문 생성")
    void testCreateOrderService() {
        // then
        assertNotNull(createdOrder.orderId());
        assertEquals("user-01", createdOrder.userId());
        assertEquals(OrderStatus.PAYMENT_REQUESTED.toString(), createdOrder.status());
        assertEquals(0, createdOrder.orderPrice());
        assertFalse(createdOrder.isRefunded());
        assertNotNull(createdOrder.address());
    }


    @Nested
    @DisplayName("주문 상태 변경 테스트")
    class OrderStatusTests {

        @Test
        @DisplayName("결제 대기 중으로 상태 변경")
        void testUpdateOrderStatusToPaymentPending() {
            // when
            OrderResponseDto updatedOrder = orderService.updateOrderStatus(
                OrderStatus.PAYMENT_PENDING, createdOrder.orderId());

            // then
            assertEquals(OrderStatus.PAYMENT_PENDING.toString(), updatedOrder.status());
        }

        @Test
        @DisplayName("결제 완료로 상태 변경")
        void testUpdateOrderStatusToPaymentCompleted() {
            // when
            OrderResponseDto updatedOrder = orderService.updateOrderStatus(
                OrderStatus.PAYMENT_COMPLETED, createdOrder.orderId());

            // then
            assertEquals(OrderStatus.PAYMENT_COMPLETED.toString(), updatedOrder.status());
        }

        @Test
        @DisplayName("배송 대기 중으로 상태 변경")
        void testUpdateOrderStatusToShippingPending() {
            // when
            OrderResponseDto updatedOrder = orderService.updateOrderStatus(
                OrderStatus.SHIPPING_PENDING, createdOrder.orderId());

            // then
            assertEquals(OrderStatus.SHIPPING_PENDING.toString(), updatedOrder.status());
        }

        @Test
        @DisplayName("배송 중으로 상태 변경")
        void testUpdateOrderStatusToShippingInProgress() {
            // when
            OrderResponseDto updatedOrder = orderService.updateOrderStatus(
                OrderStatus.SHIPPING_IN_PROGRESS, createdOrder.orderId());

            // then
            assertEquals(OrderStatus.SHIPPING_IN_PROGRESS.toString(), updatedOrder.status());
        }

        @Test
        @DisplayName("배송 완료로 상태 변경")
        void testUpdateOrderStatusToShippingCompleted() {
            // when
            OrderResponseDto updatedOrder = orderService.updateOrderStatus(
                OrderStatus.SHIPPING_COMPLETED, createdOrder.orderId());

            // then
            assertEquals(OrderStatus.SHIPPING_COMPLETED.toString(), updatedOrder.status());
        }
    }
}