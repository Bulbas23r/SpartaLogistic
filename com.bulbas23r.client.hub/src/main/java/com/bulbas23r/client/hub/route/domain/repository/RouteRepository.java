package com.bulbas23r.client.hub.route.domain.repository;

import com.bulbas23r.client.hub.route.domain.model.Route;
import java.util.List;

public interface RouteRepository {

    List<Route> saveAll(List<Route> routeList);

    Route save(Route route);
}
