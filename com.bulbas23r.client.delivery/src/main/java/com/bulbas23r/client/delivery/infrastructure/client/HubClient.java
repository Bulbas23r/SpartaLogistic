package com.bulbas23r.client.delivery.infrastructure.client;

import com.bulbas23r.client.delivery.presentation.dto.response.HubRouteResponseDto;
import common.config.FeignConfig;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hub-service", url = "http://localhost:19091/api/routes",configuration = FeignConfig.class)
public interface HubClient {

    @GetMapping("/shortest-path")
    List<UUID> getHubShortRouteList(@RequestParam UUID departureHubId, @RequestParam UUID arrivalHubId);

    @GetMapping
    HubRouteResponseDto getHubRoute(@RequestParam UUID departureHubId, @RequestParam UUID arrivalHubId);
}
