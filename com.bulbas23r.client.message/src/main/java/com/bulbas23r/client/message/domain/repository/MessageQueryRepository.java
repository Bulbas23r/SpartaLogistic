package com.bulbas23r.client.message.domain.repository;

import com.bulbas23r.client.message.domain.model.Message;
import common.utils.PageUtils;
import common.utils.PageUtils.CommonSortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public interface MessageQueryRepository {

  Page<Message> search(Pageable pageable, Direction sortDirection, CommonSortBy sortBy, String keyword);
}
