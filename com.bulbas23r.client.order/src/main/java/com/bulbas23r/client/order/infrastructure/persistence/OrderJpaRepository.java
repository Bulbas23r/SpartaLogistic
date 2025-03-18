package com.bulbas23r.client.order.infrastructure.persistence;

import com.bulbas23r.client.order.domain.model.Order;
import com.bulbas23r.client.order.domain.repository.OrderRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends OrderRepository, JpaRepository<Order, UUID> {

}
