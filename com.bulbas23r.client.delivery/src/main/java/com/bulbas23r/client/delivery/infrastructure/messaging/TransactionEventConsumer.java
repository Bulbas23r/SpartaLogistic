package com.bulbas23r.client.delivery.infrastructure.messaging;

import com.bulbas23r.client.delivery.application.service.DeliveryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.event.GroupId;
import common.event.OrderFailedEventDto;
import common.event.Topic;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionEventConsumer {

    private final ObjectMapper objectMapper;
    private final DeliveryService deliveryService;

    @KafkaListener(topics = Topic.ORDER_FAILED, groupId = GroupId.DELIVERY)
    public void handleFailedOrderEvent(Map<String, Object> event) {
        OrderFailedEventDto eventDto = objectMapper.convertValue(event, OrderFailedEventDto.class);
        deliveryService.cancelDelivery(eventDto.getOrderId());
    }
}
