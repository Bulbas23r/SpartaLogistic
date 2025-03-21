package com.bulbas23r.client.deliverymanager.application.service;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.presentation.dto.CreateDeliveryManagerRequestDto;
import com.bulbas23r.client.deliverymanager.presentation.dto.UpdateDeliveryManagerRequestDto;
import common.utils.PageUtils.CommonSortBy;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface DeliveryManagerService {

    DeliveryManager createDeliveryManager(CreateDeliveryManagerRequestDto requestDto,
        String slackId);

    DeliveryManager getDeliveryManager(Long userId);

    Page<DeliveryManager> getDeliveryManagerList(Pageable pageable);

    List<DeliveryManager> getHubDeliveryManagerList();

    List<DeliveryManager> getCompanyDeliveryManagerList(UUID hubId);

    void deleteDeliveryManager(Long userId);

    DeliveryManager updateDeliveryManager(Long userId, UpdateDeliveryManagerRequestDto requestDto);

    Page<DeliveryManager> searchDeliveryManagerList(Pageable pageable, Direction sortDirection,
        CommonSortBy sortBy, String keyword);

    boolean checkDeliveryManager(Long userId, DeliveryManager deliveryManager);
}
