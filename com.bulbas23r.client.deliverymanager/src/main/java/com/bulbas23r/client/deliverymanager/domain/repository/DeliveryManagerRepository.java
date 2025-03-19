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

    List<DeliveryManager> findAllByTypeOrderBySequenceAsc(DeliveryManagerType type);

    List<DeliveryManager> findAllByTypeAndHubIdOrderBySequenceAsc(DeliveryManagerType type,
        UUID hubId);

    Optional<DeliveryManager> findById(Long id);

    Page<DeliveryManager> findAll(Pageable pageable);
}
