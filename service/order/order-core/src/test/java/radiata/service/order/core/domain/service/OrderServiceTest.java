package radiata.service.order.core.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

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
import radiata.common.domain.order.dto.request.OrderPaymentRequestDto;
import radiata.common.domain.order.dto.response.OrderResponseDto;
import radiata.common.exception.BusinessException;
import radiata.common.message.ExceptionMessage;
import radiata.service.order.core.OrderCoreConfiguration;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.implemetation.OrderSaver;
import radiata.service.order.core.service.OrderService;
import radiata.service.order.core.service.mapper.OrderMapper;

@SpringBootTest(classes = OrderCoreConfiguration.class)
@DisplayName("주문 서비스 Test")
class OrderServiceTest {

    OrderCreateRequestDto orderCreateRequestDto;
    OrderResponseDto createdOrder;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderSaver orderSaver;
    @Autowired
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        // given
        List<OrderItemCreateRequestDto> itemList = new ArrayList<>();
        String productId = "product-0";
        String timesaleProductId = "timesaleProduct-0";
        String couponIssuedId = "couponIssued-0";

        for (int i = 1; i <= 3; i++) {
            OrderItemCreateRequestDto itemListRequestDto = new OrderItemCreateRequestDto(productId + i,
                timesaleProductId + i, couponIssuedId + i, 10, 10000);

            itemList.add(itemListRequestDto);
        }

        orderCreateRequestDto = new OrderCreateRequestDto("address-01", "comment-01", itemList);

        // 주문 생성
        createdOrder = orderService.createOrder(orderCreateRequestDto, "user-01");
    }

    @Test
    @DisplayName("주문 생성")
    void testCreateOrderService() {
        // then
        assertThat(createdOrder.orderId()).isNotNull();
        assertThat(createdOrder.userId()).isEqualTo("user-01");
        assertThat(createdOrder.status()).isEqualTo(OrderStatus.PAYMENT_REQUESTED.toString());
        assertThat(createdOrder.orderPrice()).isEqualTo(300000);
        assertThat(createdOrder.isRefunded()).isFalse();
        assertThat(createdOrder.address()).isNotNull();
        for (int i = 1; i <= createdOrder.itemList().size(); i++) {
            String expectedCoupon = "couponIssued-0" + i;

            // Set에서 해당 쿠폰이 있는지 확인
            boolean isCouponFound = createdOrder.itemList().stream()
                .anyMatch(item -> expectedCoupon.equals(item.couponIssuedId()));

            assertThat(isCouponFound).isTrue();
        }
    }

    @Nested
    @DisplayName("결제 요청 테스트")
    class OrderPaymentRequestTests {

        @Test
        @DisplayName("결제 요청 - 성공")
        void testCompletePayment1() {
            // given
            OrderPaymentRequestDto requestDto = new OrderPaymentRequestDto("paymentKey-01", 300000);

            // when
            OrderResponseDto updatedOrder = orderService.sendPaymentRequest(
                createdOrder.orderId(),
                createdOrder.userId(),
                requestDto);

            // then
            assertThat(updatedOrder.status()).isEqualTo(OrderStatus.PAYMENT_COMPLETED.toString());
        }

        @Test
        @DisplayName("결제 요청 - 실패 1(금액 불일치)")
        void testCompletePaymentFail1() {
            // given
            Order order = orderSaver.save(orderMapper.toEntity(orderCreateRequestDto, "order-01", "user-01"));
            OrderPaymentRequestDto requestDto = new OrderPaymentRequestDto("paymentKey-01", 300000);

            // when + then
            BusinessException businessException = catchThrowableOfType(
                () -> orderService.sendPaymentRequest(order.getId(), order.getUserId(), requestDto),
                BusinessException.class);

            assertThat(businessException.getMessage()).isEqualTo(ExceptionMessage.NOT_EQUALS_PRICE.getMessage());
        }
    }


    @Nested
    @DisplayName("주문 상태 변경 테스트(배송)")
    class OrderStatusTestsShipping {

        @Test
        @DisplayName("배송 대기 중 - 실패(상태 값 예외)")
        void testUpdateOrderStatusToShippingPendingFail1() {
            // when + then
            BusinessException businessException = catchThrowableOfType(
                () -> orderService.updateStatusPendingShipping(createdOrder.orderId()),
                BusinessException.class);

            assertThat(businessException.getMessage()).isEqualTo(ExceptionMessage.INVALID_ORDER_STATUS.getMessage());
        }

        @Test
        @DisplayName("배송 중 - 실패(상태 값 예외)")
        void testUpdateOrderStatusToShippingInProgress() {
            // when + then
            BusinessException businessException = catchThrowableOfType(
                () -> orderService.updateStatusShipping(createdOrder.orderId()),
                BusinessException.class);

            assertThat(businessException.getMessage()).isEqualTo(ExceptionMessage.INVALID_ORDER_STATUS.getMessage());
        }

        @Test
        @DisplayName("배송 완료 - 실패(상태 값 예외)")
        void testUpdateOrderStatusToShippingCompleted() {
            // when + then
            BusinessException businessException = catchThrowableOfType(
                () -> orderService.updateStatusCompletedShipping(createdOrder.orderId()),
                BusinessException.class);

            assertThat(businessException.getMessage()).isEqualTo(ExceptionMessage.INVALID_ORDER_STATUS.getMessage());
        }
    }
}