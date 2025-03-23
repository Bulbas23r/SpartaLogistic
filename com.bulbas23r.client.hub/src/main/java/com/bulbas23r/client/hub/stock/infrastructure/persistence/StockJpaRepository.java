package com.bulbas23r.client.hub.stock.infrastructure.persistence;

import com.bulbas23r.client.hub.stock.domain.model.Stock;
import com.bulbas23r.client.hub.stock.domain.model.StockId;
import com.bulbas23r.client.hub.stock.domain.repository.StockRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockJpaRepository extends StockRepository, JpaRepository<Stock, StockId> {

}
