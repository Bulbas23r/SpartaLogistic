package com.bulbas23r.client.hub.stock.application.service;

import com.bulbas23r.client.hub.stock.domain.model.Stock;
import com.bulbas23r.client.hub.stock.domain.repository.StockRepository;
import common.event.CreateStockEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    public Stock createStock(CreateStockEventDto eventDto) {
        Stock stock = Stock.fromEventDto(eventDto);

        return stockRepository.save(stock);
    }
}
