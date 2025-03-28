package com.bulbas23r.client.delivery.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryRouteArriveRequestDto {

    @NotNull
    private UUID deliveryId;

    @NotNull
    private UUID departureHubId;

    @NotNull
    private UUID arrivalHubId;

    private Integer distance;

    private Integer duration;

}
