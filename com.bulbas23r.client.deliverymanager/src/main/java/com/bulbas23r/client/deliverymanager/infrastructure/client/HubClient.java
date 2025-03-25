package com.bulbas23r.client.deliverymanager.infrastructure.client;

import common.config.FeignConfig;
import common.dto.HubInfoResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service", url = "http://localhost:19091/api/hubs", configuration = FeignConfig.class)
public interface HubClient {

    @GetMapping("/info/id/{hubId}")
    ResponseEntity<HubInfoResponseDto> getHubInfoById(@PathVariable("hubId") UUID hubId);

    @GetMapping("/info/managerId/{managerId}")
    ResponseEntity<HubInfoResponseDto> getHubInfoByManagerId(
        @PathVariable("managerId") Long managerId);
}
