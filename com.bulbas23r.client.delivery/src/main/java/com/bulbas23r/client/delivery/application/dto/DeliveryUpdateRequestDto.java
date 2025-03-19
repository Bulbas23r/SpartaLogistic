package com.bulbas23r.client.delivery.application.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public class DeliveryUpdateRequestDto {

    private UUID startHubId;

    private UUID endHubId;

    private UUID deliveryManagerId;

    private UUID receiverCompanyId;

    private String receiverCompanySlackId;

}
