package com.bulbas23r.client.user.infrastructure.persistence;

import com.bulbas23r.client.user.domain.model.QUser;
import com.bulbas23r.client.user.domain.model.User;
import com.bulbas23r.client.user.domain.repository.UserRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import common.utils.PageUtils;
import common.utils.PageUtils.CommonSortBy;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<User> searchUser(Pageable pageable, Direction sortDirection, CommonSortBy sortBy,
      String keyword) {
    QUser user = QUser.user;
    BooleanBuilder builder = new BooleanBuilder();

    // keyword가 존재하면 name 또는 username에 해당 키워드가 포함되는 조건을 추가
    if (keyword != null && !keyword.trim().isEmpty()) {
      builder.or(user.name.containsIgnoreCase(keyword))
          .or(user.username.containsIgnoreCase(keyword));
    }

    OrderSpecifier<?> orderSpecifier = PageUtils.getCommonOrderSpecifier(user, sortDirection,
        sortBy);

    // 페이지네이션 및 정렬 적용해서 검색 결과 리스트(content)를 가져옴
    List<User> content = queryFactory
        .selectFrom(user)
        .where(builder)
        .offset(pageable.getOffset())
        .orderBy(orderSpecifier)
        .limit(pageable.getPageSize())
        .fetch();

    // 전체 건수 조회 (페이징 정보에 필요)
    long total = Optional.ofNullable(queryFactory
        .select(user.count())
        .from(user)
        .where(builder)
        .fetchOne()).orElse(0L);

    return new PageImpl<>(content, pageable, total);
  }
}
