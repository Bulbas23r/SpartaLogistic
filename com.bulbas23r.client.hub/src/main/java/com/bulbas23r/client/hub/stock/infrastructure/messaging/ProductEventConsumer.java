package com.bulbas23r.client.hub.stock.infrastructure.messaging;

import com.bulbas23r.client.hub.stock.application.service.StockService;
import common.event.CreateStockEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEventConsumer {

    private final StockService stockService;

    @KafkaListener(topics = "create-stock")
    public void createStock(@Payload CreateStockEventDto event) {
        stockService.createStock(event);
    }
}
