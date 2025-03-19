package com.bulbas23r.client.hub.route.domain.repository;

import com.bulbas23r.client.hub.route.domain.model.Route;

public interface RouteRepository {

//    List<Route> saveAll(List<Route> routeList);

    void deleteAll();

    Route save(Route route);
}
