package radiata.service.coupon.core.infrastructure.repository;

import static radiata.service.coupon.core.domain.model.QCoupon.coupon;

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
import radiata.common.domain.coupon.dto.condition.CouponSearchCondition;
import radiata.service.coupon.core.domain.CouponQueryRepository;
import radiata.service.coupon.core.domain.model.Coupon;
import radiata.service.coupon.core.domain.model.constant.CouponSaleType;
import radiata.service.coupon.core.domain.model.constant.CouponType;

@Repository
public class CouponQueryRepositoryImpl implements CouponQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public CouponQueryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public Page<Coupon> findCouponsByCondition(CouponSearchCondition condition, Pageable pageable) {

        List<Coupon> content = queryFactory.selectFrom(coupon)
            .where(couponIdEq(condition.couponId()), couponTitleLike(condition.title()),
                couponTypeEq(condition.couponType()), couponSaleTypeEq(condition.couponSaleType()))
            .orderBy(getOrderSpecifiers(pageable.getSort()).toArray(OrderSpecifier[]::new))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> total = queryFactory.select(coupon.count())
            .from(coupon)
            .where(couponIdEq(condition.couponId()), couponTitleLike(condition.title()),
                couponTypeEq(condition.couponType()), couponSaleTypeEq(condition.couponSaleType()));

        return PageableExecutionUtils.getPage(content, pageable, total::fetchOne);
    }

    private BooleanExpression couponIdEq(String couponId) {

        return couponId != null ? coupon.id.eq(couponId) : null;
    }

    private BooleanExpression couponTitleLike(String title) {

        return title != null ? coupon.title.like(title) : null;
    }

    private BooleanExpression couponTypeEq(String couponType) {
        if (couponType != null) {
            try {
                CouponType enumCouponType = CouponType.valueOf(couponType);
                return coupon.couponType.eq(enumCouponType);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    private BooleanExpression couponSaleTypeEq(String couponSaleType) {
        if (couponSaleType != null) {
            try {
                CouponSaleType enumCouponSaleType = CouponSaleType.valueOf(couponSaleType);
                return coupon.couponSaleType.eq(enumCouponSaleType);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {

        List<OrderSpecifier> orders = sort.stream().map(o -> {
            com.querydsl.core.types.Order direction =
                o.isAscending() ? com.querydsl.core.types.Order.ASC
                    : com.querydsl.core.types.Order.DESC;
            String property = o.getProperty();
            PathBuilder<Coupon> orderByExpression = new PathBuilder<>(Coupon.class, "coupon");
            return new OrderSpecifier(direction, orderByExpression.get(property));
        }).toList();

        return orders;
    }
}
