package radiata.service.coupon.core.infrastructure.repository;

import static radiata.service.coupon.core.domain.model.QCouponIssue.couponIssue;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
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

}
