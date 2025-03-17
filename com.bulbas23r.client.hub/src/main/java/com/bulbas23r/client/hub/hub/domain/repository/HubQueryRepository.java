package com.bulbas23r.client.hub.hub.domain.repository;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import common.utils.PageUtils.CommonSortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface HubQueryRepository {

    Page<Hub> searchHub(Pageable pageable, Direction sortDirection, CommonSortBy sortBy,
        String keyword);
}
