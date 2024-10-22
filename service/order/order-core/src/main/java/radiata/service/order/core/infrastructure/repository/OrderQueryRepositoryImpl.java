package radiata.service.order.core.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;
import radiata.service.order.core.domain.model.constant.OrderStatus;
import radiata.service.order.core.domain.model.entity.Order;
import radiata.service.order.core.domain.model.entity.QOrder;
import radiata.service.order.core.domain.repository.OrderQueryRepository;

@Repository
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public OrderQueryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public List<Order> findOrdersNotCompletedWithin10Minutes() {

        QOrder order = QOrder.order;
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        return queryFactory
            .selectFrom(order)
            .where(order.updatedAt.before(tenMinutesAgo)
                .and(order.status.in(OrderStatus.PAYMENT_REQUESTED, OrderStatus.PAYMENT_PENDING)))
            .fetch();
    }
}