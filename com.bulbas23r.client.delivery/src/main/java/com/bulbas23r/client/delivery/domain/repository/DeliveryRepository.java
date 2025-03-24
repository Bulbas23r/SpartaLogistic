package com.bulbas23r.client.delivery.domain.repository;

import com.bulbas23r.client.delivery.domain.model.Delivery;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);
    Optional<Delivery> findById(UUID id);
    Page<Delivery> findAll(Pageable pageable);
}
