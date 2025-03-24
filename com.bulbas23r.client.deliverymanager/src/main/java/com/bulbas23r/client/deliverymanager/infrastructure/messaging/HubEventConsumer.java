package com.bulbas23r.client.deliverymanager.infrastructure.messaging;

import com.bulbas23r.client.deliverymanager.application.service.DeliveryManagerService;
import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import common.event.DeleteHubEventDto;
import common.event.Topic;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubEventConsumer {

    private final DeliveryManagerService deliveryManagerService;

    @KafkaListener(topics = Topic.DELETE_HUB)
    public void deleteCompanyDeliveryManagers(DeleteHubEventDto eventDto) {
        List<DeliveryManager> deliveryManagers =
            deliveryManagerService.getCompanyDeliveryManagerList(eventDto.getHubId());
        deliveryManagers.forEach(DeliveryManager::setDeleted);
    }
}
