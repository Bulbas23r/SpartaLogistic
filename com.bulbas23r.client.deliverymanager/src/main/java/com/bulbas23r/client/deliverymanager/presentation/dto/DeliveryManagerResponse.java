package com.bulbas23r.client.deliverymanager.presentation.dto;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManagerType;
import java.util.UUID;
import lombok.Data;

@Data
public class DeliveryManagerResponse {

    private Long userId;
    private UUID hubId;
    private String slackId;
    private DeliveryManagerType deliveryManagerType;
    private Integer sequence;

    public DeliveryManagerResponse(DeliveryManager deliveryManager) {
        this.userId = deliveryManager.getUserId();
        this.hubId = deliveryManager.getHubId();
        this.slackId = deliveryManager.getSlackId();
        this.deliveryManagerType = deliveryManager.getType();
        this.sequence = deliveryManager.getSequence();
    }
}
