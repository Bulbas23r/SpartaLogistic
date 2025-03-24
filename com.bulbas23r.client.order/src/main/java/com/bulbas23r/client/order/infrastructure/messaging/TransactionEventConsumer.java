package com.bulbas23r.client.order.infrastructure.messaging;

import com.bulbas23r.client.order.application.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final OrderService orderService;

    @KafkaListener(topics = Topic.ORDER_FAILED, groupId = "order-service")
    public void handleFailedOrderEvent(Map<String, Object> event) {
        OrderFailedEventDto eventDto = objectMapper.convertValue(event, OrderFailedEventDto.class);
        orderService.failOrder(eventDto.getOrderId());
    }
}
