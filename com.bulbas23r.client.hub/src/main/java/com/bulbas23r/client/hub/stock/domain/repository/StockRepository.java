package com.bulbas23r.client.hub.stock.domain.repository;

import com.bulbas23r.client.hub.stock.domain.model.Stock;

public interface StockRepository {

    Stock save(Stock stock);
}
