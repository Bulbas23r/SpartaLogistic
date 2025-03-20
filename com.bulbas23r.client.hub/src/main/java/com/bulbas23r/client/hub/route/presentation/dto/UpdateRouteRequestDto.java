package com.bulbas23r.client.hub.route.presentation.dto;

import common.annotation.ValidUUID;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class UpdateRouteRequestDto {

    @ValidUUID
    private UUID departureHubId;
    @ValidUUID
    private UUID arrivalHubId;
    @NotNull
    private Integer transitTime;
    @NotNull
    private Integer transitDistance;
}
