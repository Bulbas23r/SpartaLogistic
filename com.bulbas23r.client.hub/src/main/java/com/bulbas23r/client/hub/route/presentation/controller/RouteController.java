package com.bulbas23r.client.hub.route.presentation.controller;

import com.bulbas23r.client.hub.route.application.service.RouteService;
import com.bulbas23r.client.hub.route.domain.model.Route;
import com.bulbas23r.client.hub.route.presentation.dto.CreateRouteRequestDto;
import com.bulbas23r.client.hub.route.presentation.dto.RouteResponse;
import com.bulbas23r.client.hub.route.presentation.dto.UpdateRouteRequestDto;
import common.utils.PageUtils;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public ResponseEntity<RouteResponse> crateRoute(
        @RequestBody @Valid CreateRouteRequestDto requestDto) {
        Route route = routeService.createRoute(requestDto);

        return ResponseEntity.ok(new RouteResponse(route));
    }

    @GetMapping
    public ResponseEntity<RouteResponse> getRoute(
        @RequestParam UUID departureHubId,
        @RequestParam UUID arrivalHubId
    ) {
        Route route = routeService.getRoute(departureHubId, arrivalHubId);

        return ResponseEntity.ok(new RouteResponse(route));
    }

    @GetMapping
    public ResponseEntity<Page<RouteResponse>> getRouteList(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<Route> routeList = routeService.getRouteList(pageable);

        return ResponseEntity.ok(routeList.map(RouteResponse::new));
    }

    @PutMapping
    public ResponseEntity<RouteResponse> updateRoute(
        @RequestBody @Valid UpdateRouteRequestDto requestDto) {
        Route route = routeService.updateRoute(requestDto);

        return ResponseEntity.ok(new RouteResponse(route));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RouteResponse>> searchRoute(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "DESC", required = false) Sort.Direction sortDirection,
        @RequestParam(defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy,
        @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<Route> routeList = routeService.searchRoute(pageable, sortDirection, sortBy, keyword);

        return ResponseEntity.ok(routeList.map(RouteResponse::new));
    }
}
