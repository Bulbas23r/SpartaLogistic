package com.bulbas23r.client.delivery.presentation.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HubRouteResponseDto {

    private UUID departureHubId;
    private UUID arrivalHubId;
    private Integer transitTime;
    private Integer transitDistance;

}
