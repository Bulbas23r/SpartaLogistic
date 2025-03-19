package com.bulbas23r.client.hub.route.infrastructure.persistence;

import com.bulbas23r.client.hub.route.domain.model.QRoute;
import com.bulbas23r.client.hub.route.domain.model.Route;
import com.bulbas23r.client.hub.route.domain.repository.RouteQueryRepository;
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
public class RouteQueryRepositoryImpl implements RouteQueryRepository {

    private final JPAQueryFactory queryFactory;

    QRoute route = QRoute.route;

    @Override
    public Page<Route> searchRoute(Pageable pageable, Direction sortDirection, CommonSortBy sortBy,
        String keyword) {

        BooleanBuilder builder = new BooleanBuilder();
        if (keyword != null && !keyword.isBlank()) {
//            builder.and(route..name.containsIgnoreCase(keyword));
        }

        OrderSpecifier<?> orderSpecifier = PageUtils.getCommonOrderSpecifier(route, sortDirection,
            sortBy);

        List<Route> resultList = queryFactory
            .selectFrom(route)
            .where(builder)
            .offset(pageable.getOffset())   // 페이지네이션 적용
            .orderBy(orderSpecifier)
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = Optional.ofNullable(
            queryFactory
                .select(route.count())
                .from(route)
                .where(builder)
                .fetchOne()
        ).orElse(0L);

        return PageableExecutionUtils.getPage(resultList, pageable, () -> totalCount);
    }
}
