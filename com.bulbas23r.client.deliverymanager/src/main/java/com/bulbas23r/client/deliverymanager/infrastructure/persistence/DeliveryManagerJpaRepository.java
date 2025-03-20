package com.bulbas23r.client.deliverymanager.infrastructure.persistence;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManagerType;
import com.bulbas23r.client.deliverymanager.domain.repository.DeliveryManagerRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryManagerJpaRepository extends DeliveryManagerRepository,
    JpaRepository<DeliveryManager, Long> {

    @Query(value = "SELECT COALESCE(MAX(sequence), 0) FROM DeliveryManager WHERE type = :type")
    Integer findMaxSequenceByType(DeliveryManagerType type);

    @Query(value = "SELECT COALESCE(MAX(sequence), 0) FROM DeliveryManager WHERE type = :type and hubId = :hubId")
    Integer findMaxSequenceByTypeAndHubId(DeliveryManagerType type, UUID hubId);

    @Query("SELECT COUNT(d) FROM DeliveryManager d WHERE d.type = :type")
    Integer countByType(DeliveryManagerType type);
}
