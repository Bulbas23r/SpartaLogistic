package com.bulbas23r.client.hub.route.domain.repository;

import com.bulbas23r.client.hub.route.domain.model.Route;
import com.bulbas23r.client.hub.route.domain.model.RouteId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RouteRepository {

//    List<Route> saveAll(List<Route> routeList);

    void deleteAll();

    Route save(Route route);

    List<Route> findByActiveTrue();

    Optional<Route> findById(RouteId id);

    Page<Route> findAll(Pageable pageable);
}
