package com.bulbas23r.client.deliverymanager.infrastructure.messaging;

import com.bulbas23r.client.deliverymanager.application.service.DeliveryManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.event.DeleteHubEventDto;
import common.event.GroupId;
import common.event.Topic;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubEventConsumer {

    private final DeliveryManagerService deliveryManagerService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topic.DELETE_HUB, groupId = GroupId.DELIVERY_MANAGER)
    public void deleteCompanyDeliveryManagers(Map<String, Object> event) {
        log.info("Received delete-hub event");
        DeleteHubEventDto eventDto = objectMapper.convertValue(event, DeleteHubEventDto.class);

        deliveryManagerService.deleteCompanyDeliveryManagers(eventDto.getHubId());
    }
}
