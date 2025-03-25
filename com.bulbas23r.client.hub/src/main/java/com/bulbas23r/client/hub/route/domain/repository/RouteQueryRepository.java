package com.bulbas23r.client.hub.route.domain.repository;

import com.bulbas23r.client.hub.route.domain.model.Route;
import common.utils.PageUtils.CommonSortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface RouteQueryRepository {

    Page<Route> searchRoute(Pageable pageable, Direction sortDirection, CommonSortBy sortBy,
        String keyword);
}
