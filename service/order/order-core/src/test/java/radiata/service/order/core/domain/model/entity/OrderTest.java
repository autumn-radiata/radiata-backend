package radiata.service.order.core.domain.model.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.github.ksuid.KsuidGenerator;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import radiata.service.order.core.domain.repository.OrderRepository;

@SpringBootTest
class OrderTest {

    @Autowired
    private OrderRepository orderRepository;  // 생성자 대신 필드 주입

    private Order order;

    @BeforeEach
    @DisplayName("Order Entity 생성 테스트")
    void createOrder() {
        String orderId = KsuidGenerator.generate();
        String userId = KsuidGenerator.generate();

        // Order 생성
        order = Order.create(
            orderId,
            userId,
            100000,
            "Test Address",
            "Test Comment"
        );

        // OrderItem 리스트 생성
        Set<OrderItem> orderItems = new HashSet<>();
        String id = KsuidGenerator.generate();
        for (int i = 1; i <= 3; i++) {
            String orderItemId = id + i;
            String productId = KsuidGenerator.generate() + '-' + i;
            String couponId = KsuidGenerator.generate() + '-' + i;
            String rewardId = KsuidGenerator.generate() + '-' + i;
            Integer quantity = i * 10;
            Integer unitPrice = 10000;

            // OrderItem 생성
            OrderItem orderItem = OrderItem.create(
                orderItemId,
                order,
                productId,
                couponId,
                rewardId,
                quantity,
                unitPrice
            );

            // OrderItem을 Set에 추가
            orderItems.add(orderItem);
        }

        // Order에 OrderItem 추가
        order.setOrderItems(orderItems);

        // Order 저장 (OrderItems는 cascade로 함께 저장)
        orderRepository.save(order);
    }

    @Test
    @DisplayName("Order 객체 조회 테스트")
    void getOrder() {

        // Order 조회
        Order orderInfo = orderRepository.findById(order.getId())
            .orElseThrow(() -> new RuntimeException("Order not found"));

        System.out.println("#########[ 주문 정보 ] ######### ");
        // Order 객체의 필드 출력
        System.out.println("Order ID: " + order.getId());
        System.out.println("User ID: " + order.getUserId());
        System.out.println("Total Amount: " + order.getOrderPrice());
        System.out.println("Address: " + order.getAddress());
        System.out.println("Comment: " + order.getComment());
        // 추가적으로 OrderItem 출력
        System.out.println("---------------------------------------------");
        System.out.println("#########[ 주문한 상품 리스트 정보]#########");
        for (OrderItem item : order.getItemList()) {
            System.out.println("Order Item ID: " + item.getId());
            System.out.println("Order ID: " + item.getOrder().getId());
            System.out.println("Product ID: " + item.getProductId());
            System.out.println("Coupon Id " + item.getCouponIssuedId());
            System.out.println("Reward Id " + item.getRewardPointId());
            System.out.println("Quantity: " + item.getQuantity());
            System.out.println("Unit Price: " + item.getUnitPrice());
            System.out.println("Payment Price: " + item.getPaymentPrice());
            System.out.println();
        }

        // Assert 문으로 특정 값 확인
        assertNotNull(order, "Order should not be null");
    }
}