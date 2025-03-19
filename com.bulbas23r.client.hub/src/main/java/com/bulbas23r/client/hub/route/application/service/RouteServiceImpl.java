package com.bulbas23r.client.hub.route.application.service;

import com.bulbas23r.client.hub.hub.application.service.HubService;
import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.route.domain.model.HubConnection;
import com.bulbas23r.client.hub.route.domain.model.Route;
import com.bulbas23r.client.hub.route.domain.model.RouteId;
import com.bulbas23r.client.hub.route.domain.repository.RouteRepository;
import com.bulbas23r.client.hub.route.domain.service.PathFinderService;
import com.bulbas23r.client.hub.route.infrastructure.dto.DrivingResponse;
import com.bulbas23r.client.hub.route.infrastructure.service.NaverApiService;
import com.bulbas23r.client.hub.route.presentation.dto.CreateRouteRequestDto;
import common.exception.NotFoundException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final HubService hubService;
    private final HubConnection hubConnection;
    private final RouteRepository routeRepository;
    private final NaverApiService naverApiService;
    private final PathFinderService pathFinderService;

    @Override
    public void initializeRoute() {
        routeRepository.deleteAll();
        List<Hub> hubList = hubService.getActiveHubList();
        Map<String, Hub> hubMap = hubList.stream()
            .collect(Collectors.toMap(Hub::getName, hub -> hub));

        Map<String, List<String>> connections = hubConnection.getConnections();
//        List<Route> saveRouteList = new ArrayList<>(
        for (Map.Entry<String, List<String>> entry : connections.entrySet()) {
            String key = entry.getKey();
            Hub hubX = hubMap.get(key);
            List<String> connectedHubList = entry.getValue();

            for (String hubName : connectedHubList) {
                Hub hubY = hubMap.get(hubName);
                if (hubY != null) {
                    Mono<DrivingResponse> xToYMono = naverApiService.drivingApi(
                        hubX.getLatitude(), hubX.getLongitude(),
                        hubY.getLatitude(), hubY.getLongitude());
                    Mono<DrivingResponse> yToXMono = naverApiService.drivingApi(
                        hubY.getLatitude(), hubY.getLongitude(),
                        hubX.getLatitude(), hubX.getLongitude());
                    xToYMono.zipWith(yToXMono).doOnSuccess(results -> {
                        DrivingResponse xToY = results.getT1();
                        DrivingResponse yToX = results.getT2();

                        routeRepository.save(
                            new Route(hubX, hubY, xToY.getDuration(), xToY.getDistance()));
                        routeRepository.save(
                            new Route(hubY, hubX, yToX.getDuration(), yToX.getDistance()));
                    }).subscribe();
//                    saveRouteList.add(new Route(hubX, hubY, 0, 0));
//                    saveRouteList.add(new Route(hubY, hubX, 0, 0));
                }
            }
        }
//        routeRepository.saveAll(saveRouteList);
    }

    @Override
    public List<UUID> getShortestPath(UUID departureHubId, UUID arrivalHubId) {
        List<Hub> hubs = hubService.getActiveHubList();
        List<Route> routes = routeRepository.findByActiveTrue();

        return pathFinderService.findShortestPath(hubs, routes, departureHubId, arrivalHubId);
    }

    @Override
    public Route createRoute(CreateRouteRequestDto requestDto) {
        Route route = new Route(requestDto);
        return routeRepository.save(route);
    }

    @Override
    public Route getRoute(UUID departureHubId, UUID arrivalHubId) {
        RouteId routeId = new RouteId(departureHubId, arrivalHubId);

        return routeRepository.findById(routeId).orElseThrow(
            () -> new NotFoundException("존재하지 않는 경로입니다!")
        );
    }
}
