package com.bulbas23r.client.hub.route.application.service;

import com.bulbas23r.client.hub.hub.application.service.HubService;
import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.route.domain.model.HubConnection;
import com.bulbas23r.client.hub.route.domain.model.Route;
import com.bulbas23r.client.hub.route.domain.repository.RouteRepository;
import com.bulbas23r.client.hub.route.infrastructure.dto.DrivingResponse;
import com.bulbas23r.client.hub.route.infrastructure.service.NaverApiService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final HubService hubService;
    private final HubConnection hubConnection;
    private final RouteRepository routeRepository;
    private final NaverApiService naverApiService;

    @Override
    public void initializeRoute() {
        routeRepository.deleteAll();
        List<Hub> hubList = hubService.getActiveHubList();
        Map<String, Hub> hubMap = hubList.stream()
            .collect(Collectors.toMap(Hub::getName, hub -> hub));

        Map<String, List<String>> connections = hubConnection.getConnections();
//        List<Route> saveRouteList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : connections.entrySet()) {
            String key = entry.getKey();
            Hub hubX = hubMap.get(key);
            List<String> connectedHubList = entry.getValue();

            for (String hubName : connectedHubList) {
                Hub hubY = hubMap.get(hubName);
                if (hubY != null) {
                    // TODO 경로 API로 소요시간, 거리 따서 양방향 경로 넣기
                    DrivingResponse xToY = naverApiService.drivingApi(
                        hubX.getLatitude(), hubX.getLongitude(),
                        hubY.getLatitude(), hubY.getLongitude());
                    DrivingResponse yToX = naverApiService.drivingApi(
                        hubY.getLatitude(), hubY.getLongitude(),
                        hubX.getLatitude(), hubX.getLongitude());
                    routeRepository.save(
                        new Route(hubX, hubY, xToY.getDuration(), xToY.getDistance()));
                    routeRepository.save(
                        new Route(hubY, hubX, yToX.getDuration(), yToX.getDistance()));
//                    saveRouteList.add(new Route(hubX, hubY, 0, 0));
//                    saveRouteList.add(new Route(hubY, hubX, 0, 0));
                }
            }
        }
//        routeRepository.saveAll(saveRouteList);
    }
}
