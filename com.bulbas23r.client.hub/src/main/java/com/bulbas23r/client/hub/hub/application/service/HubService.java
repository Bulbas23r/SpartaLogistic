package com.bulbas23r.client.hub.hub.application.service;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.hub.presentation.dto.request.CreateHubRequestDto;
import com.bulbas23r.client.hub.hub.presentation.dto.request.UpdateHubRequestDto;
import common.utils.PageUtils.CommonSortBy;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface HubService {

    Hub createHub(CreateHubRequestDto requestDto);

    Hub updateHub(UUID hubId, UpdateHubRequestDto requestDto);

    Hub getHubById(UUID hubId);

    Hub getHubByManagerId(Long managerId);

    Page<Hub> getHubList(Pageable pageable);

    List<Hub> getActiveHubList();

    void deleteHub(UUID hubId);

    Page<Hub> searchHub(Pageable pageable, Direction sortDirection, CommonSortBy sortBy,
        String keyword);

    void deleteRoutesByHubId(UUID hubId);

    void deleteStocksByProductId(UUID productId);
}
