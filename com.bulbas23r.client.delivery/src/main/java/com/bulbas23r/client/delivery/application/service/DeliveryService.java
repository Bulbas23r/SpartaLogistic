package com.bulbas23r.client.delivery.application.service;

import com.bulbas23r.client.delivery.application.dto.DeliveryCreateRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryResponseDto;
import com.bulbas23r.client.delivery.application.dto.DeliverySearchRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryUpdateRequestDto;
import com.bulbas23r.client.delivery.domain.model.DeliveryStatus;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryService {
    DeliveryResponseDto createDelivery(DeliveryCreateRequestDto requestDto);
    DeliveryResponseDto getDelivery(UUID deliveryId);
    Page<DeliveryResponseDto> getDeliveryList(Pageable pageable);
    DeliveryResponseDto updateDelivery(UUID id, DeliveryUpdateRequestDto requestDto);
    DeliveryResponseDto deleteDelivery(UUID deliveryId);
    Page<DeliveryResponseDto> searchDelivery(DeliverySearchRequestDto requestDto);
    void changeStatus(UUID deliveryId, DeliveryStatus status);
}
