package com.bulbas23r.client.hub.route.infrastructure.persistence;

import com.bulbas23r.client.hub.route.domain.model.Route;
import com.bulbas23r.client.hub.route.domain.model.RouteId;
import com.bulbas23r.client.hub.route.domain.repository.RouteRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteJpaRepository extends RouteRepository, JpaRepository<Route, RouteId> {

}
