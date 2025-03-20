package com.bulbas23r.client.deliverymanager.infrastructure.persistence;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.domain.model.QDeliveryManager;
import com.bulbas23r.client.deliverymanager.domain.repository.DeliveryManagerQueryRepository;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryManagerQueryRepositoryImpl implements DeliveryManagerQueryRepository {

    private final JPAQueryFactory queryFactory;

    QDeliveryManager deliveryManager = QDeliveryManager.deliveryManager;

    @Override
    public Page<DeliveryManager> searchDeliveryManager(Pageable pageable, Direction sortDirection,
        CommonSortBy sortBy, String keyword) {

        BooleanBuilder builder = new BooleanBuilder();
        if (keyword != null && !keyword.isBlank()) {
            builder.and(deliveryManager.slackId.containsIgnoreCase(keyword));
        }

        OrderSpecifier<?> orderSpecifier = PageUtils.getCommonOrderSpecifier(deliveryManager,
            sortDirection,
            sortBy);

        List<DeliveryManager> resultList = queryFactory
            .selectFrom(deliveryManager)
            .where(builder)
            .offset(pageable.getOffset())   // 페이지네이션 적용
            .orderBy(orderSpecifier)
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = Optional.ofNullable(
            queryFactory
                .select(deliveryManager.count())
                .from(deliveryManager)
                .where(builder)
                .fetchOne()
        ).orElse(0L);

        return PageableExecutionUtils.getPage(resultList, pageable, () -> totalCount);
    }
}
