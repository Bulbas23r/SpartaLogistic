package com.bulbas23r.client.order.domain.repository;

import com.bulbas23r.client.order.domain.model.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(UUID orderId);
    void update(Order order);
    Page<Order> findAllByIsDeletedIsFalse(Pageable pageable);
}
