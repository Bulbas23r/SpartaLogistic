package com.bulbas23r.client.deliverymanager.infrastructure.persistence;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.domain.repository.DeliveryManagerRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryManagerJpaRepository extends DeliveryManagerRepository,
    JpaRepository<DeliveryManager, Long> {

}
