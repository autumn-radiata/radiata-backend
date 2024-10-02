package radiata.service.order.core.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import radiata.common.domain.order.dto.request.OrderCreateRequestDto;
import radiata.common.domain.order.dto.request.OrderItemCreateRequestDto;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 서비스 Test")
class OrderServiceImplTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderServiceImpl orderService;

    OrderCreateRequestDto orderCreateDto;
    Order order;

    @BeforeEach
    void setUp() {
        // 주문 생성 요청 DTO 설정
        List<OrderItemCreateRequestDto> orderItemCreateRequestDtoList = new ArrayList<>();
        String productId = "product-0";
        String timesaleId = "timesale-0";
        String couponId = "coupon-0";
        String rewardId = "reward-0";

        for (int i = 1; i <= 3; i++) {
            OrderItemCreateRequestDto orderItemCreateDto = new OrderItemCreateRequestDto(
                productId + i,
                timesaleId + i,
                couponId + i,
                rewardId + i,
                i);

            orderItemCreateRequestDtoList.add(orderItemCreateDto);
        }

        orderCreateDto = new OrderCreateRequestDto(
            "address-01",
            "comment-01",
            orderItemCreateRequestDtoList);

        // 주문 객체 미리 생성
        order = Order.of(
            "order-01",
            "user-01",
            "address-01",
            "comment-01"
        );
    }

    @Nested
    @DisplayName("주문 생성 테스트")
    class 주문_생성_테스트 {

        //TODO orderItem 관련해서 해야되는데 순서가 기준?을 잡지 못함. - 추후 구현
        @Test
        @DisplayName("주문 생성")
        void testCreateOrderService() {

            // when: 주문 생성 로직 실행
            Order createdOrder = orderService.createOrder(orderCreateDto, "order-01", "user-01");

            // then: 주문 객체의 필드 값 확인
            assertEquals("order-01", createdOrder.getId());
            assertEquals("user-01", createdOrder.getUserId());
            assertEquals(OrderStatus.PAYMENT_REQUESTED, createdOrder.getStatus());
            assertEquals(0, createdOrder.getOrderPrice());
            assertFalse(createdOrder.getIsRefunded());
            assertNotNull(createdOrder.getAddress());
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 테스트")
    class 주문_상태_변경_테스트 {

        @Test
        @DisplayName("주문 상태 변경 - 결제 대기 중")
        void testUpdateOrderStatus1() {
            // given: 주문 조회 시 모의 동작 설정
            given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));

            // when: 주문 상태를 결제 대기 중으로 변경
            orderService.updateOrderStatus(order.getId(), OrderStatus.PAYMENT_PENDING);

            // then: 상태가 제대로 변경되었는지 확인
            assertEquals(OrderStatus.PAYMENT_PENDING, order.getStatus());
        }
    }
}