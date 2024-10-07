package radiata.service.timesale.core.infrastructure.repository;

import static radiata.service.timesale.core.domain.QTimeSale.timeSale;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import radiata.common.domain.timesale.dto.condition.TimeSaleSearchCondition;
import radiata.service.timesale.core.domain.TimeSale;
import radiata.service.timesale.core.domain.repository.TimeSaleQueryRepository;

@Repository
public class TImeSaleQueryRepositoryImpl implements TimeSaleQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public TImeSaleQueryRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<TimeSale> findTimeSalesByCondition(TimeSaleSearchCondition condition, Pageable pageable) {

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
