package com.bulbas23r.client.product.infrastructure.persisitence;

import com.bulbas23r.client.product.domain.model.Product;
import com.bulbas23r.client.product.domain.model.QProduct;
import com.bulbas23r.client.product.domain.repository.ProductQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import common.utils.PageUtils;
import common.utils.PageUtils.CommonSortBy;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    QProduct product = QProduct.product;

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable, Direction sortDirection,
        CommonSortBy sortBy) {

        BooleanBuilder builder = new BooleanBuilder();
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(product.name.containsIgnoreCase(keyword));
        }

        OrderSpecifier<?> orderSpecifier = PageUtils.getCommonOrderSpecifier(product, sortDirection, sortBy);

        List<Product> products = queryFactory
            .selectFrom(product)
            .where(builder)
            .offset(pageable.getOffset())
            .orderBy(orderSpecifier)
            .limit(pageable.getPageSize())
            .fetch();

        Long totalCount = Optional.ofNullable(
            queryFactory
                .select(product.count())
                .from(product)
                .where(builder)
                .fetchOne()
        ).orElse(0L);

        return PageableExecutionUtils.getPage(products, pageable, () -> totalCount);
    }
}
