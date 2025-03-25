package com.bulbas23r.client.hub.route.domain.repository;

import com.bulbas23r.client.hub.route.domain.model.Route;
import com.bulbas23r.client.hub.route.domain.model.RouteId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RouteRepository {

//    List<Route> saveAll(List<Route> routeList);

    void deleteAll();

    Route save(Route route);

    List<Route> findByActiveTrue();

    Optional<Route> findById(RouteId id);

    Page<Route> findAll(Pageable pageable);

    @Query("SELECT r FROM Route r WHERE r.id.departureHubId = :hubId OR r.id.arrivalHubId = :hubId")
    List<Route> findAllByHubId(@Param("hubId") UUID hubId);
}
