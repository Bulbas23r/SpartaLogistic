package com.bulbas23r.client.hub.stock.domain.repository;

import com.bulbas23r.client.hub.stock.domain.model.Stock;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository {

    Stock save(Stock stock);

    List<Stock> findById_HubIdAndId_ItemIdIn(UUID hubId, List<UUID> itemIds);

    @Query("SELECT s FROM Stock s WHERE s.id.hubId = :hubId")
    List<Stock> findAllByHubId(UUID hubId);
}
