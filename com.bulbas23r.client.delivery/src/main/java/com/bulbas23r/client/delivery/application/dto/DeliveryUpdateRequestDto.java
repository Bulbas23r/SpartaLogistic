package com.bulbas23r.client.delivery.application.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public class DeliveryUpdateRequestDto {

    private UUID departureHubId;

    private UUID arrivalHubIdHubId;

    private UUID deliveryManagerId;

    private UUID receiverCompanyId;

    private String receiverCompanySlackId;

}
