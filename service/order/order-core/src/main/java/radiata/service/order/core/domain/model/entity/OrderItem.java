package radiata.service.order.core.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String productId;

    private String couponIssuedId;

    @Column(nullable = false)
    private Integer quantity;

//    @Column(nullable = false)
//    private size;

    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    private Integer paymentPrice;


    public static OrderItem of(
        String id,
        Order order,
        String productId,
        String couponIssuedId,
        Integer quantity,
        Integer unitPrice) {

        // TODO - 쿠폰&적립금 형식 정해지면 paymentPrice 계산 추가
        return OrderItem.builder()
            .id(id)
            .order(order)
            .productId(productId)
            .couponIssuedId(couponIssuedId)
            .quantity(quantity)
            .unitPrice(unitPrice)
            .paymentPrice(unitPrice * quantity)
            .build();
    }
}
