package com.bulbas23r.client.hub.route.application.service;

import com.bulbas23r.client.hub.route.domain.model.Route;
import com.bulbas23r.client.hub.route.presentation.dto.CreateRouteRequestDto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RouteService {

    void initializeRoute();

    List<UUID> getShortestPath(UUID departureHubId, UUID arrivalHubId);

    Route createRoute(CreateRouteRequestDto requestDto);

    Route getRoute(UUID departureHubId, UUID arrivalHubId);

    Page<Route> getRouteList(Pageable pageable);
}
