package com.bulbas23r.client.delivery.infrastructure.persistence;

import com.bulbas23r.client.delivery.application.dto.DeliveryResponseDto;
import com.bulbas23r.client.delivery.domain.model.QDelivery;
import com.bulbas23r.client.delivery.domain.repository.DeliveryQueryRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import common.utils.PageUtils;
import common.utils.PageUtils.CommonSortBy;
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
public class DeliveryQueryRepositoryImpl implements DeliveryQueryRepository {

    private final JPAQueryFactory queryFactory;

    QDelivery delivery = QDelivery.delivery;


    public Page<DeliveryResponseDto> searchDelivery(UUID orderId ,UUID startHubId, UUID endHubId,
        UUID deliveryManagerId, UUID receiverCompanyId, Pageable pageable, Direction sortDirection,
        CommonSortBy sortBy) {

        BooleanExpression whereClause = buildWhereClause(orderId, startHubId,endHubId,deliveryManagerId,receiverCompanyId);

        OrderSpecifier<?> orderSpecifier = PageUtils.getCommonOrderSpecifier(delivery,sortDirection,sortBy);

        List<DeliveryResponseDto> deliveryList = queryFactory
            .select(Projections.constructor(DeliveryResponseDto.class,
                delivery.id,
                delivery.orderId,
                delivery.startHubId,
                delivery.endHubId,
                delivery.status.stringValue(),
                delivery.deliveryManagerId,
                delivery.receiverCompanyId,
                delivery.receiverCompanySlackId
                ))
            .from(delivery)
            .where(whereClause)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(orderSpecifier)
            .fetch();

        long totalCount = Optional.ofNullable(
            queryFactory
                .select(delivery.count())
                .from(delivery)
                .where(whereClause)
                .fetchOne()
        ).orElse(0L);

        return PageableExecutionUtils.getPage(deliveryList, pageable, () -> totalCount);
    }


    private BooleanExpression buildWhereClause(UUID orderId, UUID startHubId, UUID endHubId,
        UUID deliveryManagerId, UUID receiverCompanyId) {

        BooleanExpression whereClause = delivery.isDeleted.eq(false);

        if (orderId != null) { whereClause = whereClause.and(delivery.orderId.eq(orderId)); }
        if (startHubId != null) whereClause = whereClause.and(delivery.startHubId.eq(startHubId));
        if (endHubId != null) whereClause = whereClause.and(delivery.endHubId.eq(endHubId));
        if (deliveryManagerId != null) whereClause = whereClause.and(delivery.deliveryManagerId.eq(deliveryManagerId));
        if (receiverCompanyId !=null) whereClause = whereClause.and(delivery.receiverCompanyId.eq(receiverCompanyId));

        return whereClause;
    }
}
