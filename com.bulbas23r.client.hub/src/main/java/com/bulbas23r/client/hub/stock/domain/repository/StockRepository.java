package com.bulbas23r.client.hub.stock.domain.repository;

import com.bulbas23r.client.hub.stock.domain.model.Stock;
import java.util.List;
import java.util.UUID;

public interface StockRepository {

    Stock save(Stock stock);

    List<Stock> findById_HubIdAndId_ItemIdIn(UUID hubId, List<UUID> itemIds);
}
