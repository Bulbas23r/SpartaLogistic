package com.bulbas23r.client.hub.route.presentation.dto;

import com.bulbas23r.client.hub.route.domain.model.Route;
import java.util.UUID;
import lombok.Data;

@Data
public class RouteResponse {

    private UUID departureHubId;
    private UUID arrivalHubId;
    private Integer transitTime;
    private Integer transitDistance;

    public RouteResponse(Route route) {
        this.departureHubId = route.getId().getDepartureHubId();
        this.arrivalHubId = route.getId().getArrivalHubId();
        this.transitTime = route.getTransitTime();
        this.transitDistance = route.getTransitDistance();
    }
}
