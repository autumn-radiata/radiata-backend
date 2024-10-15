package radiata.service.order.core.domain.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import radiata.database.model.BaseEntity;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.payment.core.domain.model.vo.PaymentType;

@Entity
@Table(name = "r_order")
@Getter
@Builder // 테스트 용
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order extends BaseEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private Integer orderPrice;

    @Column(nullable = false)
    private Boolean isRefunded;

    @Column(nullable = false)
    private String address;

    private String paymentId;

    private PaymentType paymentType;
    // TODO - 사용 예정
    private Integer usedPoint;

    private String comment;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems;

    // 주문 생성
    public static Order of(
        String id,
        String userId,
        String address,
        String comment) {

        return Order.builder()
            .id(id)
            .userId(userId)
            .status(OrderStatus.PAYMENT_REQUESTED)
            .orderPrice(0)
            .isRefunded(false)
            .address(address)
            .comment(comment)
            .build();
    }

    public void setOrderPriceAndItems(Integer orderPrice, Set<OrderItem> orderItems) {
        this.orderPrice = orderPrice; // TODO- this.usedPoint 빼야할 걸?
        this.orderItems = orderItems;
    }

    public void setPaymentIdAndType(String paymentId, PaymentType paymentType) {
        this.paymentId = paymentId;
        this.paymentType = paymentType;
    }

    public void usePoint(Integer usedPoint) {
        this.usedPoint = usedPoint;
    }


    // 주문 상태 변경
    public void updateOrderStatus(
        OrderStatus status
    ) {
        this.status = status;
    }

    // 주문 삭제
}
