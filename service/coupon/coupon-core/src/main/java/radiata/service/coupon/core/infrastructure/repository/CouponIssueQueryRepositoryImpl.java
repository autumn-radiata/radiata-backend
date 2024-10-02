package radiata.service.coupon.core.infrastructure.repository;

import static radiata.service.coupon.core.domain.model.QCouponIssue.couponIssue;

import com.querydsl.core.types.OrderSpecifier;
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
import radiata.common.domain.coupon.dto.response.CouponIssueResponseDto;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.model.CouponIssue;
import radiata.service.coupon.core.domain.repository.CouponIssueQueryRepository;

@Repository
public class CouponIssueQueryRepositoryImpl implements CouponIssueQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public CouponIssueQueryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    public CouponIssue findFirstCouponIssue(String couponId, String userId) {

        return queryFactory.selectFrom(couponIssue)
            .where(couponIssue.coupon.id.eq(couponId))
            .where(couponIssue.userId.eq(userId))
            .fetchFirst();
    }

    @Override
    public Page<CouponIssue> findCouponIssuesByUserId(String userId, Pageable pageable) {

        List<CouponIssue> content = queryFactory.selectFrom(couponIssue)
            .where(couponIssue.userId.eq(userId))
            .orderBy(getOrderSpecifiers(pageable.getSort()).toArray(OrderSpecifier[]::new))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        JPAQuery<Long> total = queryFactory.select(couponIssue.count())
            .from(couponIssue)
            .where(couponIssue.userId.eq(userId));

        return PageableExecutionUtils.getPage(content, pageable, total::fetchOne);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {

        List<OrderSpecifier> orders = sort.stream().map(o -> {
            com.querydsl.core.types.Order direction =
                o.isAscending() ? com.querydsl.core.types.Order.ASC
                    : com.querydsl.core.types.Order.DESC;
            String property = o.getProperty();
            PathBuilder<CouponIssue> orderByExpression = new PathBuilder<>(CouponIssue.class, "couponIssue");
            return new OrderSpecifier(direction, orderByExpression.get(property));
        }).toList();

        return orders;
    }
}
