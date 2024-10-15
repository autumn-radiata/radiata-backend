package radiata.service.brand.core.infrastructure;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import radiata.service.brand.core.model.constant.ColorType;
import radiata.service.brand.core.model.constant.GenderType;
import radiata.service.brand.core.model.constant.SizeType;
import radiata.service.brand.core.model.entity.Brand;
import radiata.service.brand.core.model.entity.Category;
import radiata.service.brand.core.model.entity.Product;
import radiata.service.brand.core.model.entity.QProduct;
import radiata.service.brand.core.model.repository.ProductQueryDslRepository;

@Repository
public class ProductQueryDslRepositoryImpl implements ProductQueryDslRepository {


    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public ProductQueryDslRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Page<Product> findProductsByCondition(Brand brand, Category category, GenderType gender, SizeType size,
        ColorType color, String query, Pageable pageable) {

        QProduct product = QProduct.product;
        BooleanBuilder builder = new BooleanBuilder();

        // 추가적인 동적 조건 추가
        if (brand != null) {
            builder.and(product.brand.eq(brand));
        }
        if (category != null) {
            builder.and(product.category.eq(category));
        }
        if (gender != null) {
            builder.and(product.gender.eq(gender));
        }
        if (size != null) {
            builder.and(product.size.eq(size));
        }
        if (color != null) {
            builder.and(product.color.eq(color));
        }
        if (query != null && !query.isEmpty()) {
            builder.and(product.name.containsIgnoreCase(query));
        }

        // 페이징 처리
        List<Product> products = queryFactory
            .selectFrom(product)
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(product.createdAt.asc()) // 정렬
            .fetch();

        // 전체 카운트를 가져오는 쿼리
        JPAQuery<Long> countQuery = queryFactory
            .select(product.count())
            .from(product)
            .where(builder);

        return PageableExecutionUtils.getPage(products, pageable, countQuery::fetchOne);
    }
}
