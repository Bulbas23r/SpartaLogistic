package com.bulbas23r.client.delivery.application.service;

import com.bulbas23r.client.delivery.application.dto.DeliveryRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryResponseDto;

public interface DeliveryService {
    DeliveryResponseDto createDelivery(DeliveryRequestDto requestDto);
}
