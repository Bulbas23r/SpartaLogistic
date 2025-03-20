package com.bulbas23r.client.hub.route.presentation.controller;

import com.bulbas23r.client.hub.route.application.service.RouteService;
import com.bulbas23r.client.hub.route.domain.model.Route;
import com.bulbas23r.client.hub.route.presentation.dto.CreateRouteRequestDto;
import com.bulbas23r.client.hub.route.presentation.dto.RouteResponse;
import com.bulbas23r.client.hub.route.presentation.dto.UpdateRouteRequestDto;
import common.utils.PageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Route", description = "허브 간 이동정보 관련 API")
public class RouteController {

    private final RouteService routeService;

    // TODO master role check 추가하기
    @PostMapping("/init")
    @Operation(summary = "허브 간 이동정보 초기화하기")
    public ResponseEntity<Void> init() {
        routeService.initializeRoute();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shortest-path")
    @Operation(summary = "최단 경로 가져오기")
    public ResponseEntity<List<UUID>> getShortestRoutes(
        @RequestParam UUID departureHubId,
        @RequestParam UUID arrivalHubId
    ) {
        List<UUID> shortestPath = routeService.getShortestPath(departureHubId, arrivalHubId);
        return ResponseEntity.ok(shortestPath);
    }

    @PostMapping
    @Operation(summary = "허브 간 이동정보 생성하기")
    public ResponseEntity<RouteResponse> crateRoute(
        @RequestBody @Valid CreateRouteRequestDto requestDto) {
        Route route = routeService.createRoute(requestDto);

        return ResponseEntity.ok(new RouteResponse(route));
    }

    @GetMapping
    @Operation(summary = "허브 간 이동정보 단건 조회")
    public ResponseEntity<RouteResponse> getRoute(
        @RequestParam UUID departureHubId,
        @RequestParam UUID arrivalHubId
    ) {
        Route route = routeService.getRoute(departureHubId, arrivalHubId);

        return ResponseEntity.ok(new RouteResponse(route));
    }

    @GetMapping("/list")
    @Operation(summary = "허브 간 이동정보 리스트 조회")
    public ResponseEntity<Page<RouteResponse>> getRouteList(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<Route> routeList = routeService.getRouteList(pageable);

        return ResponseEntity.ok(routeList.map(RouteResponse::new));
    }

    @PutMapping
    @Operation(summary = "허브 간 이동정보 수정하기")
    public ResponseEntity<RouteResponse> updateRoute(
        @RequestBody @Valid UpdateRouteRequestDto requestDto) {
        Route route = routeService.updateRoute(requestDto);

        return ResponseEntity.ok(new RouteResponse(route));
    }

    @GetMapping("/search")
    @Operation(summary = "허브 간 이동정보 검색하기")
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
