package com.bulbas23r.client.delivery.application.service;

import com.bulbas23r.client.delivery.application.dto.DeliveryRouteArriveRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryRouteDepartRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryRouteResponseDto;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRouteService {
    DeliveryRouteResponseDto departDeliveryRoute(DeliveryRouteDepartRequestDto requestDto);
    DeliveryRouteResponseDto arriveDeliveryRoute(@Valid DeliveryRouteArriveRequestDto requestDto);
    Page<DeliveryRouteResponseDto> getDeliveryRouteList(UUID deliveryId,Pageable pageable);
}
