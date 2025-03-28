package com.bulbas23r.client.delivery.domain.repository;

import com.bulbas23r.client.delivery.domain.model.DeliveryRoute;
import com.bulbas23r.client.delivery.domain.model.DeliveryRouteStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRouteRepository {

    Optional<DeliveryRoute> findByDelivery_IdAndDepartureHubIdAndArrivalHubIdAndStatus(
        UUID deliveryId, UUID departureHubId, UUID arrivalHubId, DeliveryRouteStatus status);

    boolean isPreviousSequenceDeparted(UUID deliveryId, Integer sequence);

    Page<DeliveryRoute> findAllByDelivery_Id(UUID deliveryId, Pageable pageable);

    Page<DeliveryRoute> findAllByDelivery_OrderId(UUID orderId, Pageable pageable);
}
