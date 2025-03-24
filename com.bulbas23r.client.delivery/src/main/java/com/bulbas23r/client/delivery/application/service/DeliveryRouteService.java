package com.bulbas23r.client.delivery.application.service;

import com.bulbas23r.client.delivery.presentation.dto.request.DeliveryRouteArriveRequestDto;
import com.bulbas23r.client.delivery.presentation.dto.request.DeliveryRouteDepartRequestDto;
import com.bulbas23r.client.delivery.presentation.dto.response.DeliveryRouteResponseDto;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRouteService {

  DeliveryRouteResponseDto departDeliveryRoute(DeliveryRouteDepartRequestDto requestDto);

  DeliveryRouteResponseDto arriveDeliveryRoute(@Valid DeliveryRouteArriveRequestDto requestDto);

  Page<DeliveryRouteResponseDto> getDeliveryRouteList(UUID deliveryId, Pageable pageable);

  Page<DeliveryRouteResponseDto> getDeliveryByOrderIdRouteList(UUID orderId, Pageable pageable);
}
