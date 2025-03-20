package com.bulbas23r.client.deliverymanager.domain.repository;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManagerType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryManagerRepository {

    DeliveryManager save(DeliveryManager deliveryManager);

    boolean existsByTypeAndSequence(DeliveryManagerType type, Integer sequence);

    boolean existsByTypeAndHubIdAndSequence(DeliveryManagerType type, UUID hubId, Integer sequence);

    Integer findMaxSequenceByType(DeliveryManagerType type);

    Integer findMaxSequenceByTypeAndHubId(DeliveryManagerType type, UUID hubId);

    List<DeliveryManager> findAllByTypeOrderBySequenceAsc(DeliveryManagerType type);

    List<DeliveryManager> findAllByTypeAndHubIdOrderBySequenceAsc(DeliveryManagerType type,
        UUID hubId);

    Optional<DeliveryManager> findById(Long id);

    Page<DeliveryManager> findAll(Pageable pageable);
}
