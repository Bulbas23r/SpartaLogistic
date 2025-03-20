package com.bulbas23r.client.hub.route.application.service;

import java.util.List;
import java.util.UUID;

public interface RouteService {

    void initializeRoute();

    List<UUID> getShortestPath(UUID departureHubId, UUID arrivalHubId);
}
