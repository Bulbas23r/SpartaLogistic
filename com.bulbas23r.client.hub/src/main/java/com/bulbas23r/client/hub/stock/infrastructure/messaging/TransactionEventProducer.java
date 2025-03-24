package com.bulbas23r.client.hub.stock.infrastructure.messaging;

import common.event.OrderFailedEventDto;
import common.event.Topic;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderFailedEvent(UUID orderId) {
        kafkaTemplate.send(Topic.ORDER_FAILED, new OrderFailedEventDto(orderId));
    }
}
