package com.bulbas23r.client.hub.stock.infrastructure.messaging;

import com.bulbas23r.client.hub.stock.application.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.event.CreateOrderEventDto;
import common.event.Topic;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final StockService stockService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = {Topic.CREATE_ORDER, Topic.CANCEL_ORDER})
    public void updateStock(Map<String, Object> event) {
        CreateOrderEventDto eventDto = objectMapper.convertValue(event, CreateOrderEventDto.class);
        stockService.updateStock(eventDto);
    }
}
