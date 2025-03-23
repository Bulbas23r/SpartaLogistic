package com.bulbas23r.client.hub.stock.application.service;

import com.bulbas23r.client.hub.stock.domain.model.Stock;
import common.event.CreateStockEventDto;
import common.event.UpdateStockEventDto;
import java.util.UUID;

public interface StockService {

    Stock createStock(CreateStockEventDto eventDto);

    void updateStock(UpdateStockEventDto eventDto);

    void deleteStocksByHubId(UUID hubId);

    void deleteStocksByProductId(UUID productId);
}
