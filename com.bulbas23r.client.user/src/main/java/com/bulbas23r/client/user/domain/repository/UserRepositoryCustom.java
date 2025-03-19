package com.bulbas23r.client.user.domain.repository;

import com.bulbas23r.client.user.domain.model.User;
import common.utils.PageUtils;
import common.utils.PageUtils.CommonSortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public interface UserRepositoryCustom {

  Page<User> searchUser(Pageable pageable, Direction sortDirection, CommonSortBy sortBy, String keyword);
}
