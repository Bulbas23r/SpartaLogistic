package com.bulbas23r.client.delivery.infrastructure.persistence;

import com.bulbas23r.client.delivery.domain.model.Delivery;
import com.bulbas23r.client.delivery.domain.repository.DeliveryRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryJpaRepository extends JpaRepository<Delivery, UUID>, DeliveryRepository {
    Page<Delivery> findAll(Pageable pageable);
}
