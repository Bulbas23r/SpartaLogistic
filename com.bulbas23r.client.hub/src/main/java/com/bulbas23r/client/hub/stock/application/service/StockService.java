package com.bulbas23r.client.hub.stock.application.service;

import com.bulbas23r.client.hub.stock.domain.model.Stock;
import common.event.CreateOrderEventDto;
import common.event.CreateProductEventDto;

public interface StockService {

    Stock createStock(CreateProductEventDto eventDto);

    void updateStock(CreateOrderEventDto eventDto);
}
