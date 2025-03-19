package com.bulbas23r.client.deliverymanager.application.service;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.presentation.dto.CreateDeliveryManagerRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryManagerService {

    DeliveryManager createDeliveryManager(CreateDeliveryManagerRequestDto requestDto);

    DeliveryManager getDeliveryManager(Long userId);

    Page<DeliveryManager> getDeliveryManagerList(Pageable pageable);
}
