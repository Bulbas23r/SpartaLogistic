package com.bulbas23r.client.deliverymanager.application.service;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.presentation.dto.CreateDeliveryManagerRequestDto;

public interface DeliveryManagerService {

    DeliveryManager createDeliveryManager(CreateDeliveryManagerRequestDto requestDto);

    DeliveryManager getDeliveryManager(Long userId);
}
