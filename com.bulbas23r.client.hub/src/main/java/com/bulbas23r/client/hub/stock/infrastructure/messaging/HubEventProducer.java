package com.bulbas23r.client.hub.stock.infrastructure.messaging;

import common.event.DeleteHubEventDto;
import common.event.Topic;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendDeleteHubEvent(UUID hubId) {
        kafkaTemplate.send(Topic.DELETE_HUB, new DeleteHubEventDto(hubId));
    }
}
