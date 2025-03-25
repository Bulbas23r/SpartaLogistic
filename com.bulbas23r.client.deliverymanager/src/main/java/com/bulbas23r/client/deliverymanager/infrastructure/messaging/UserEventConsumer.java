package com.bulbas23r.client.deliverymanager.infrastructure.messaging;

import com.bulbas23r.client.deliverymanager.application.service.DeliveryManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.event.DeleteUserEventDto;
import common.event.GroupId;
import common.event.Topic;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventConsumer {

    private final DeliveryManagerService deliveryManagerService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topic.DELETE_USER, groupId = GroupId.DELIVERY_MANAGER)
    public void deleteDeliveryManager(Map<String, Object> event) {
        DeleteUserEventDto eventDto = objectMapper.convertValue(event, DeleteUserEventDto.class);
        deliveryManagerService.deleteDeliveryManager(eventDto.getUserId());
    }
}
