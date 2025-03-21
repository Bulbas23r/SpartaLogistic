package com.bulbas23r.client.delivery.application.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryRouteDepartRequestDto {

    @NotNull
    private UUID deliveryManagerId;

    @NotNull
    private UUID deliveryId;

    @NotNull
    private UUID departureHubId;

    @NotNull
    private UUID arrivalHubId;


}
