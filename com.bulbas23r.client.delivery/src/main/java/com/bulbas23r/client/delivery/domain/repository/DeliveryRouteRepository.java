package com.bulbas23r.client.delivery.domain.repository;

import com.bulbas23r.client.delivery.domain.model.DeliveryRoute;
import com.bulbas23r.client.delivery.domain.model.DeliveryRouteStatus;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRouteRepository {

    DeliveryRoute save(DeliveryRoute deliveryRoute);
    Optional<DeliveryRoute> findByDelivery_IdAndDepartureHubIdAndArrivalHubIdAndStatus(
        UUID deliveryId, UUID departureHubId, UUID arrivalHubId, DeliveryRouteStatus status);
    boolean isPreviousSequenceDeparted(UUID deliveryId, Integer sequence);

}
