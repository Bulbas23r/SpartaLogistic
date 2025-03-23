package com.bulbas23r.client.hub.stock.domain.repository;

import com.bulbas23r.client.hub.stock.domain.model.Stock;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository {

    Stock save(Stock stock);

    @Query("SELECT s FROM Stock s WHERE s.id.hubId = :hubId AND s.id.productId IN :productIds")
    List<Stock> findByHubIdAndProductId(UUID hubId, List<UUID> productIds);

    @Query("SELECT s FROM Stock s WHERE s.id.hubId = :hubId")
    List<Stock> findAllByHubId(UUID hubId);

    @Query("SELECT s FROM Stock s WHERE s.id.productId = :productId")
    List<Stock> findAllByProductId(UUID productId);
}
