package com.bulbas23r.client.delivery.application.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class HubRouteResponseDto {

    private UUID departureHubId;
    private UUID arrivalHubId;
    private Integer transitTime;
    private Integer transitDistance;

}
