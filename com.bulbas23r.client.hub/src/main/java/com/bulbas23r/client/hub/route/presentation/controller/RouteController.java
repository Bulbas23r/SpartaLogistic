package com.bulbas23r.client.hub.route.presentation.controller;

import com.bulbas23r.client.hub.route.application.service.RouteService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    // TODO master role check 추가하기
    @PostMapping("/init")
    public ResponseEntity<Void> init() {
        routeService.initializeRoute();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shortest-path")
    public ResponseEntity<List<UUID>> getShortestRoutes(
        @RequestParam UUID departureHubId,
        @RequestParam UUID arrivalHubId
    ) {
        List<UUID> shortestPath = routeService.getShortestPath(departureHubId, arrivalHubId);
        return ResponseEntity.ok(shortestPath);
    }
}
