package com.bulbas23r.client.hub.hub.infrastructure.persistence;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.hub.domain.model.QHub;
import com.bulbas23r.client.hub.hub.domain.repository.HubQueryRepository;
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
public class HubQueryRepositoryImpl implements HubQueryRepository {

    private final JPAQueryFactory queryFactory;

    QHub hub = QHub.hub;

    @Override
    public Page<Hub> searchHub(Pageable pageable, Direction sortDirection, CommonSortBy sortBy,
        String keyword) {

        BooleanBuilder builder = new BooleanBuilder();
        if (keyword != null && !keyword.isBlank()) {
            builder.and(hub.name.containsIgnoreCase(keyword));
        }

        OrderSpecifier<?> orderSpecifier = PageUtils.getCommonOrderSpecifier(hub, sortDirection,
            sortBy);

        List<Hub> resultList = queryFactory
            .selectFrom(hub)
            .where(builder)
            .offset(pageable.getOffset())   // 페이지네이션 적용
            .orderBy(orderSpecifier)
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = Optional.ofNullable(
            queryFactory
                .select(hub.count())
                .from(hub)
                .where(builder)
                .fetchOne()
        ).orElse(0L);

        return PageableExecutionUtils.getPage(resultList, pageable, () -> totalCount);
    }
}
