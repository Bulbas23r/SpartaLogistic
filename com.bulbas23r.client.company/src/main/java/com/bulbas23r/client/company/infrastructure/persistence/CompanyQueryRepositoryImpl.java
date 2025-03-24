package com.bulbas23r.client.company.infrastructure.persistence;

import ch.qos.logback.core.util.StringUtil;
import com.bulbas23r.client.company.presentation.dto.CompanyResponseDto;
import com.bulbas23r.client.company.domain.model.CompanyType;
import com.bulbas23r.client.company.domain.model.QCompany;
import com.bulbas23r.client.company.domain.repository.CompanyQueryRepository;
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
public class CompanyQueryRepositoryImpl implements CompanyQueryRepository {

    private final JPAQueryFactory queryFactory;

    QCompany company = QCompany.company;

    public Page<CompanyResponseDto> searchCompany(String name, UUID hubId , CompanyType type, Pageable pageable,
        Direction sortDirection, CommonSortBy sortBy) {

        BooleanExpression whereClause = buildWhereClause(name,type,hubId);

        OrderSpecifier<?> orderSpecifier = PageUtils.getCommonOrderSpecifier(company,sortDirection,sortBy);

        List<CompanyResponseDto> companyList = queryFactory
            .select(Projections.constructor(CompanyResponseDto.class,
                company.id,
                company.hubId,
                company.managerId,
                company.name,
                company.type.stringValue(),
                company.phone,
                company.address.roadAddress,
                company.address.jibunAddress,
                company.address.city,
                company.address.district,
                company.address.town,
                company.address.postalCode
                ))
            .from(company)
            .where(whereClause)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(orderSpecifier)
            .fetch();

        long totalCount = Optional.ofNullable(
            queryFactory
                .select(company.count())
                .from(company)
                .where(whereClause)
                .fetchOne()
        ).orElse(0L);

        return PageableExecutionUtils.getPage(companyList, pageable, () -> totalCount);
    }

    private BooleanExpression buildWhereClause(String name, CompanyType type, UUID hubId) {

        BooleanExpression whereClause = company.isDeleted.eq(false);

        if(!StringUtil.isNullOrEmpty(name)) {
            whereClause = whereClause.and(company.name.containsIgnoreCase(name));
        }

        if(hubId != null) {
            whereClause = whereClause.and(company.hubId.eq(hubId));
        }

        if(type != null) {
            whereClause = whereClause.and(company.type.eq(type));
        }

        return whereClause;
    }

}
