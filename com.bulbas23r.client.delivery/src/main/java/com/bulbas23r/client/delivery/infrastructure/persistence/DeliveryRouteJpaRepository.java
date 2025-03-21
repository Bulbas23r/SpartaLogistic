package com.bulbas23r.client.delivery.infrastructure.persistence;

import com.bulbas23r.client.delivery.domain.model.DeliveryRoute;
import com.bulbas23r.client.delivery.domain.model.DeliveryRouteStatus;
import com.bulbas23r.client.delivery.domain.repository.DeliveryRouteRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryRouteJpaRepository extends JpaRepository<DeliveryRoute, UUID>, DeliveryRouteRepository {
    Optional<DeliveryRoute> findByDelivery_IdAndDepartureHubIdAndArrivalHubIdAndStatus(UUID deliveryId, UUID departureHubId, UUID arrivalHubId, DeliveryRouteStatus status);

    @Query("SELECT COUNT(d) > 0 FROM DeliveryRoute d " +
        "WHERE d.delivery.id = :deliveryId " +
        "AND d.sequence = :sequence - 1 " +
        "AND d.status = 'HUB_DEPARTURE'")
    boolean isPreviousSequenceDeparted(UUID deliveryId, Integer sequence);

    Page<DeliveryRoute> findAllByDelivery_Id(UUID deliveryId, Pageable pageable);
}
