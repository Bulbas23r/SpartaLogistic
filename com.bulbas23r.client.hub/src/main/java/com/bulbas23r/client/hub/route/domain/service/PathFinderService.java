package com.bulbas23r.client.hub.route.domain.service;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.route.domain.model.Route;
import java.util.List;
import java.util.UUID;

public interface PathFinderService {

    List<UUID> findShortestPath(List<Hub> hubs, List<Route> routes, UUID departureHubId,
        UUID arrivalHubId);
}
