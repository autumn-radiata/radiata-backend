package radiata.service.timesale.core.infrastructure.repository;

import static radiata.service.timesale.core.domain.QTimeSale.timeSale;
import static radiata.service.timesale.core.domain.QTimeSaleProduct.timeSaleProduct;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import radiata.common.domain.timesale.dto.condition.TimeSaleSearchCondition;
import radiata.service.timesale.core.domain.TimeSale;
import radiata.service.timesale.core.domain.TimeSaleProduct;
import radiata.service.timesale.core.domain.repository.TimeSaleQueryRepository;

@Repository
public class TimeSaleQueryRepositoryImpl implements TimeSaleQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public TimeSaleQueryRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<TimeSale> findTimeSalesByCondition(TimeSaleSearchCondition condition,
        Pageable pageable) {

        List<TimeSale> content = queryFactory.selectFrom(timeSale)
            .where(titleEq(condition.title()))
            .orderBy(getOrderSpecifiers(pageable.getSort()).toArray(OrderSpecifier[]::new))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> total = queryFactory.select(timeSale.count())
            .from(timeSale)
            .where(titleEq(condition.title()));

        return PageableExecutionUtils.getPage(content, pageable, total::fetchOne);
    }

    @Override
    public Optional<TimeSale> findByProductId(String productId) {

        LocalDateTime now = LocalDateTime.now();

        return Optional.ofNullable(queryFactory.select(timeSale)
            .from(timeSale)
            .leftJoin(timeSaleProduct).on(timeSaleProduct.timeSale.id.eq(timeSale.id))
            .where(
                timeSaleProduct.productId.eq(productId),
                timeSaleProduct.timeSaleStartTime.before(now),
                timeSaleProduct.timeSaleEndTime.after(now)
            )
            .fetchOne());
    }

    @Override
    public List<TimeSaleProduct> findByProductIds(List<String> productIds) {

        LocalDateTime now = LocalDateTime.now();

        return queryFactory.select(timeSaleProduct)  // 타임세일 상품을 선택
            .from(timeSaleProduct)  // 타임세일 상품을 기준으로 쿼리
            .leftJoin(timeSale).on(timeSale.id.eq(timeSaleProduct.timeSale.id))  // 타임세일과 조인
            .where(
                timeSaleProduct.productId.in(productIds),  // 주어진 상품 ID 리스트에 포함된 상품
                timeSaleProduct.timeSaleStartTime.before(now),  // 타임세일 시작 시간 전에
                timeSaleProduct.timeSaleEndTime.after(now)  // 타임세일 종료 시간 이후인 상품
            )
            .fetch();  // 결과를 리스트로 반환
    }


    private BooleanExpression titleEq(String title) {

        return title != null ? timeSale.title.contains(title) : null;
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {

        List<OrderSpecifier> orders = sort.stream().map(o -> {
            com.querydsl.core.types.Order direction =
                o.isAscending() ? com.querydsl.core.types.Order.ASC
                    : com.querydsl.core.types.Order.DESC;
            String property = o.getProperty();
            PathBuilder<TimeSale> orderByExpression = new PathBuilder<>(TimeSale.class, "timeSale");
            return new OrderSpecifier(direction, orderByExpression.get(property));
        }).toList();

        return orders;
    }
}
