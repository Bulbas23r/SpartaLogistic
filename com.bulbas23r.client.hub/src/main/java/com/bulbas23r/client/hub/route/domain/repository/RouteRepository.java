package com.bulbas23r.client.hub.route.domain.repository;

import com.bulbas23r.client.hub.route.domain.model.Route;
import java.util.List;

public interface RouteRepository {

    void saveAll(List<Route> routeList);
}
