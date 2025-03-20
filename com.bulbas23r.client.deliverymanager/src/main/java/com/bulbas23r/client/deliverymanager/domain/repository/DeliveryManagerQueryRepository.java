package com.bulbas23r.client.deliverymanager.domain.repository;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import common.utils.PageUtils.CommonSortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface DeliveryManagerQueryRepository {

    Page<DeliveryManager> searchDeliveryManager(Pageable pageable, Direction sortDirection,
        CommonSortBy sortBy,
        String keyword);
}
