package com.bulbas23r.client.hub.route.domain.repository;

import com.bulbas23r.client.hub.route.domain.model.Route;
import com.bulbas23r.client.hub.route.domain.model.RouteId;
import java.util.List;
import java.util.Optional;

public interface RouteRepository {

//    List<Route> saveAll(List<Route> routeList);

    void deleteAll();

    Route save(Route route);

    List<Route> findByActiveTrue();

    Optional<Route> findById(RouteId id);
}
