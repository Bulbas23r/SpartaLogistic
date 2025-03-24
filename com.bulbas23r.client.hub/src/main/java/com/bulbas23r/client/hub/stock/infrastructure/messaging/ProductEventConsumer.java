package com.bulbas23r.client.hub.stock.infrastructure.messaging;

import com.bulbas23r.client.hub.hub.application.service.HubService;
import com.bulbas23r.client.hub.stock.application.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.event.CreateProductEventDto;
import common.event.DeleteProductDto;
import common.event.Topic;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEventConsumer {

    private final StockService stockService;
    private final HubService hubService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topic.CREATE_PRODUCT)
    public void createStock(Map<String, Object> event) {
        CreateProductEventDto eventDto = objectMapper.convertValue(event,
            CreateProductEventDto.class);
        stockService.createStock(eventDto);
    }

    @KafkaListener(topics = Topic.DELETE_PRODUCT)
    public void deleteStock(Map<String, Object> event) {
        DeleteProductDto eventDto = objectMapper.convertValue(event, DeleteProductDto.class);
        hubService.deleteStocksByProductId(eventDto.getProductId());
    }
}
