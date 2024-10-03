package radiata.service.order.core.implemetation;

import org.springframework.transaction.annotation.Transactional;
import radiata.common.annotation.Implementation;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;

@Implementation
public class OrderUpdater {

    // TODO 결제(대기중, 완료), 배송(중, 대기중, 완료) - 사용 예정
    // 주문 상태 변경
    @Transactional
    public void updateOrderStatus( OrderStatus updateStatus, Order order) {

        order.updateOrderStatus(updateStatus);
    }
}
