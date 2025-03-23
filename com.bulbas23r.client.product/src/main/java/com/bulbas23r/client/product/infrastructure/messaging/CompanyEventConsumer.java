package com.bulbas23r.client.product.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.event.UpdateStockEventDto;
import java.util.Map;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CompanyEventConsumer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public CompanyEventConsumer(KafkaTemplate<String, Object> kafkaTemplate,
        ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-test-update-events")
    public void handleCompanyDeleteEvent(Map<String, Object> eventMap) {
        UpdateStockEventDto event = objectMapper.convertValue(eventMap, UpdateStockEventDto.class);
        System.out.println(event);
    }
}
