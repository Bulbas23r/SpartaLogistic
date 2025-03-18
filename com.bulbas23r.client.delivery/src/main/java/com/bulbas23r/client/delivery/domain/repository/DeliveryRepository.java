package com.bulbas23r.client.delivery.domain.repository;

import com.bulbas23r.client.delivery.domain.model.Delivery;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);
}
