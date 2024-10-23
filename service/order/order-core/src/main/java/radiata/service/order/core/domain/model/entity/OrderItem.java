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
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "r_order_item")
@Getter
@Builder // 테스트 용
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItem extends BaseEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String productId;

    private String timeSaleProductId;

    private String couponIssuedId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    private Integer paymentPrice;


    public static OrderItem of(
        String id,
        Order order,
        String productId,
        String timeSaleProductId,
        String couponIssuedId,
        Integer quantity,
        Integer unitPrice) {

        return OrderItem.builder()
            .id(id)
            .order(order)
            .productId(productId)
            .timeSaleProductId(timeSaleProductId)
            .couponIssuedId(couponIssuedId)
            .quantity(quantity)
            .unitPrice(unitPrice)
            .paymentPrice(quantity * unitPrice)
            .build();
    }

    // 금액 지정
    public void setPrice(String saleType, Integer discountValue) {
        this.paymentPrice = calculateDiscount(saleType, discountValue);
    }

    // 할인 금액, 율 계산
    private int calculateDiscount(String saleType, Integer discountValue) {
        double finalPrice = saleType.equals("AMOUNT")
            ? this.paymentPrice - discountValue                 // 금액 할인
            : this.paymentPrice * (1 - discountValue / 100.0);  // 할인율 적용

        return (int) finalPrice;
    }
}
