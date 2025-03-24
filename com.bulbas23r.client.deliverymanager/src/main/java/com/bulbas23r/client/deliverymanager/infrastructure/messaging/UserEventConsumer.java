package com.bulbas23r.client.deliverymanager.infrastructure.messaging;

import com.bulbas23r.client.deliverymanager.application.service.DeliveryManagerService;
import common.event.DeleteUserEventDto;
import common.event.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventConsumer {

    private final DeliveryManagerService deliveryManagerService;

    @KafkaListener(topics = Topic.DELETE_USER)
    public void deleteDeliveryManager(DeleteUserEventDto eventDto) {
        deliveryManagerService.deleteDeliveryManager(eventDto.getUserId());
    }
}
