package com.bulbas23r.client.order.infrastructure.persistence;

import com.bulbas23r.client.order.domain.model.Order;
import com.bulbas23r.client.order.domain.model.OrderStatus;
import com.bulbas23r.client.order.domain.model.QOrder;
import com.bulbas23r.client.order.domain.model.QOrderProduct;
import com.bulbas23r.client.order.domain.repository.OrderQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import common.utils.PageUtils;
import common.utils.PageUtils.CommonSortBy;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    QOrder order = QOrder.order;
    QOrderProduct orderProduct = QOrderProduct.orderProduct;

    @Override
    // 주문 ID 또는 주문 제품 이름으로 검색
    public Page<Order> searchOrders(String keyword, Pageable pageable, Direction sortDirection,
        CommonSortBy sortBy) {

        BooleanBuilder builder = new BooleanBuilder();
        if(keyword != null && !keyword.isEmpty()) {
            builder.and(
                Expressions.stringTemplate("{0}::text", order.id)
                    .containsIgnoreCase(keyword)
                    .or(orderProduct.productName.containsIgnoreCase(keyword))
            );
        }

        OrderSpecifier<?> orderSpecifier = PageUtils.getCommonOrderSpecifier(order, sortDirection, sortBy);

        List<Order> orders = queryFactory
            .selectFrom(order)
            .leftJoin(order.orderProducts, orderProduct).fetchJoin()
            .where(builder)
            .offset(pageable.getOffset())
            .orderBy(orderSpecifier)
            .limit(pageable.getPageSize())
            .fetch();

        Long totalCount = Optional.ofNullable(
            queryFactory
                .select(order.count())
                .from(order)
                .leftJoin(order.orderProducts, orderProduct)
                .where(builder)
                .fetchOne()
        ).orElse(0L);

        return PageableExecutionUtils.getPage(orders, pageable, () -> totalCount);
    }

    @Override
    @Transactional
    public void updateOrderStatus(UUID orderId, OrderStatus orderStatus) {
        queryFactory
            .update(order)
            .set(order.status, orderStatus)
            .where(order.id.eq(orderId))
            .execute();
    }
}
