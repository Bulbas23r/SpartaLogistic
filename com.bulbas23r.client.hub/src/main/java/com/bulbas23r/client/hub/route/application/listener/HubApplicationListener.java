package com.bulbas23r.client.hub.route.application.listener;

import com.bulbas23r.client.hub.route.application.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class HubApplicationListener {

    private final RouteService routeService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeRoute() {
        routeService.initializeRoute();
        log.info("initialize route information");
    }
}
