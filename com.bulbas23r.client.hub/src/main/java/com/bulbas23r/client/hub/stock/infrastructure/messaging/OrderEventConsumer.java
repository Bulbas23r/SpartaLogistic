package com.bulbas23r.client.hub.stock.infrastructure.messaging;

import com.bulbas23r.client.hub.stock.application.service.StockService;
import common.event.TopicName;
import common.event.UpdateStockEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final StockService stockService;

    @KafkaListener(topics = {TopicName.CREATE_ORDER, TopicName.CANCEL_ORDER})
    public void updateStock(@Payload UpdateStockEventDto event) {
        stockService.updateStock(event);
    }
}
