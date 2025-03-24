package com.bulbas23r.client.hub.stock.infrastructure.messaging;

import com.bulbas23r.client.hub.hub.application.service.HubService;
import com.bulbas23r.client.hub.stock.application.service.StockService;
import common.event.CreateStockEventDto;
import common.event.DeleteProductDto;
import common.event.TopicName;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEventConsumer {

    private final StockService stockService;
    private final HubService hubService;

    @KafkaListener(topics = TopicName.CREATE_PRODUCT)
    public void createStock(@Payload CreateStockEventDto event) {
        stockService.createStock(event);
    }

    @KafkaListener(topics = TopicName.DELETE_PRODUCT)
    public void deleteStock(@Payload DeleteProductDto event) {
        hubService.deleteStocksByProductId(event.getProductId());
    }
}
