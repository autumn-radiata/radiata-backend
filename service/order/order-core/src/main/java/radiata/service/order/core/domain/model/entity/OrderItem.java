package radiata.service.order.core.domain.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "r_order_item")
@Getter
@Builder // 테스트 용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItem {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String productId;

    private String couponIssuedId;

    private String rewardPointId;

    @Column(nullable = false)
    private Integer quantity;

//    @Column(nullable = false)
//    private size;

    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    private Integer paymentPrice;


    public static OrderItem create(
        String id,
        Order order,
        String productId,
        String couponIssuedId,
        String rewardPointId,
        Integer quantity,
        Integer unitPrice) {

        // TODO - 쿠폰&적립금 형식 정해지면 paymentPrice 계산 추가
        return OrderItem.builder()
            .id(id)
            .order(order)
            .productId(productId)
            .couponIssuedId(couponIssuedId)
            .rewardPointId(rewardPointId)
            .quantity(quantity)
            .unitPrice(unitPrice)
            .paymentPrice(unitPrice * quantity)
            .build();
    }
}
