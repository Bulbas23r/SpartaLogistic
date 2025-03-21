package com.bulbas23r.client.message.infrastructure.persistence;

import com.bulbas23r.client.message.domain.model.Message;
import com.bulbas23r.client.message.domain.model.QMessage;
import com.bulbas23r.client.message.domain.repository.MessageQueryRepository;
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
public class MessageQueryRepositoryImpl implements MessageQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Message> search(Pageable pageable, Direction sortDirection, CommonSortBy sortBy,
      String keyword) {
    QMessage qMessage = QMessage.message1;
    BooleanBuilder builder = new BooleanBuilder();

    // 키워드가 있으면 message 필드에 포함되어있는지 검색 (대소문자 구분없이)
    if (keyword != null && !keyword.trim().isEmpty()) {
      builder.and(qMessage.message.containsIgnoreCase(keyword));
    }

    OrderSpecifier<?> orderSpecifier = PageUtils.getCommonOrderSpecifier(qMessage, sortDirection,
        sortBy);

    // 페이지네이션 및 정렬 적용해서 검색 결과 리스트(content)를 가져옴
    List<Message> content = queryFactory
        .selectFrom(qMessage)
        .where(builder)
        .offset(pageable.getOffset())
        .orderBy(orderSpecifier)
        .limit(pageable.getPageSize())
        .fetch();

    // 전체 건수 조회 (페이징 정보에 필요)
    long total = Optional.ofNullable(queryFactory
        .select(qMessage.count())
        .from(qMessage)
        .where(builder)
        .fetchOne()).orElse(0L);

    return new PageImpl<>(content, pageable, total);
  }
}
