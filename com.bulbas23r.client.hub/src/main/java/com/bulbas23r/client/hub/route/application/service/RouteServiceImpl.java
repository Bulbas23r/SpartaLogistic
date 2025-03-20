package com.bulbas23r.client.hub.route.application.service;

import com.bulbas23r.client.hub.hub.application.service.HubService;
import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.route.domain.model.HubConnection;
import com.bulbas23r.client.hub.route.domain.model.Route;
import com.bulbas23r.client.hub.route.domain.model.RouteId;
import com.bulbas23r.client.hub.route.domain.repository.RouteQueryRepository;
import com.bulbas23r.client.hub.route.domain.repository.RouteRepository;
import com.bulbas23r.client.hub.route.domain.service.PathFinderService;
import com.bulbas23r.client.hub.route.infrastructure.dto.DrivingResponse;
import com.bulbas23r.client.hub.route.infrastructure.service.NaverApiService;
import com.bulbas23r.client.hub.route.presentation.dto.CreateRouteRequestDto;
import com.bulbas23r.client.hub.route.presentation.dto.UpdateRouteRequestDto;
import common.exception.BadRequestException;
import common.exception.NotFoundException;
import common.utils.PageUtils.CommonSortBy;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final HubService hubService;
    private final HubConnection hubConnection;
    private final RouteRepository routeRepository;
    private final NaverApiService naverApiService;
    private final PathFinderService pathFinderService;
    private final RouteQueryRepository routeQueryRepository;

    @Transactional
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

    @Transactional(readOnly = true)
    @Override
    public List<UUID> getShortestPath(UUID departureHubId, UUID arrivalHubId) {
        List<Hub> hubs = hubService.getActiveHubList();
        List<Route> routes = routeRepository.findByActiveTrue();

        return pathFinderService.findShortestPath(hubs, routes, departureHubId, arrivalHubId);
    }

    @Transactional
    @Override
    public Route createRoute(CreateRouteRequestDto requestDto) {
        Route route = new Route(requestDto);
        return routeRepository.save(route);
    }

    @Transactional(readOnly = true)
    @Override
    public Route getRoute(UUID departureHubId, UUID arrivalHubId) {
        RouteId routeId = new RouteId(departureHubId, arrivalHubId);

        return routeRepository.findById(routeId).orElseThrow(
            () -> new NotFoundException("존재하지 않는 경로입니다!")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Route> getRouteList(Pageable pageable) {
        return routeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Route updateRoute(UpdateRouteRequestDto requestDto) {
        Route route = getRoute(requestDto.getDepartureHubId(), requestDto.getArrivalHubId());
        route.update(requestDto);

        return route;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Route> searchRoute(Pageable pageable, Direction sortDirection, CommonSortBy sortBy,
        String keyword) {
        return routeQueryRepository.searchRoute(pageable, sortDirection, sortBy, keyword);
    }


    @Transactional
    @Override
    public void deleteRoute(UUID departureHubId, UUID arrivalHubId) {
        Route route = getRoute(departureHubId, arrivalHubId);

        if (route.isActive()) {
            throw new BadRequestException("활성화된 이동 정보는 삭제할 수 없습니다!");
        }

        route.setDeleted();
    }
}
