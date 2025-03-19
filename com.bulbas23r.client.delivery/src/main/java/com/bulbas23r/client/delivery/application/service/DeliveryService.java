package com.bulbas23r.client.delivery.application.service;

import com.bulbas23r.client.delivery.application.dto.DeliveryRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryService {
    DeliveryResponseDto createDelivery(DeliveryRequestDto requestDto);
    DeliveryResponseDto getDelivery(UUID deliveryId);
    Page<DeliveryResponseDto> getDeliveryList(Pageable pageable);
}
