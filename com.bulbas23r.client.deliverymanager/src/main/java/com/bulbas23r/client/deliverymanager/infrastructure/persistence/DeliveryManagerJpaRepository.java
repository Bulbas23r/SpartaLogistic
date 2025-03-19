package com.bulbas23r.client.deliverymanager.infrastructure.persistence;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManagerType;
import com.bulbas23r.client.deliverymanager.domain.repository.DeliveryManagerRepository;
import jakarta.persistence.LockModeType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryManagerJpaRepository extends DeliveryManagerRepository,
    JpaRepository<DeliveryManager, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT COALESCE(MAX(sequence), 0) FROM DeliveryManager WHERE type = :type")
    Integer findMaxSequenceByType(DeliveryManagerType type);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT COALESCE(MAX(sequence), 0) FROM DeliveryManager WHERE type = :type and hubId = :hubId")
    Integer findMaxSequenceByTypeAndHubId(DeliveryManagerType type, UUID hubId);
}
