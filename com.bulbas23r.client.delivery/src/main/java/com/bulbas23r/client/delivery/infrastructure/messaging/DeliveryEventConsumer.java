package com.bulbas23r.client.delivery.infrastructure.messaging;


import com.bulbas23r.client.delivery.application.service.DeliveryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.event.CreateOrderEventDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryEventConsumer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final DeliveryService deliveryService;

    @Transactional
    @KafkaListener(topics = "create-order-delivery")
    public void handleOrderCreateEvent(Map<String, Object> eventMap) {
        CreateOrderEventDto eventDto = objectMapper.convertValue(eventMap, CreateOrderEventDto.class);
        System.out.println("CreateOrderEventDto!: " + eventDto);

       deliveryService.createDeliveryByOrder(eventDto);

    }

}
