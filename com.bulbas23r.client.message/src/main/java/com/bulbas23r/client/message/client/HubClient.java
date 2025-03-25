package com.bulbas23r.client.message.client;

import common.config.FeignConfig;
import common.dto.HubInfoResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "hub-service", url = "http://localhost:19091/api/hubs", configuration = FeignConfig.class)
@FeignClient(name = "hub-service", url = "${hub-service.url}", configuration = FeignConfig.class)
public interface HubClient {
  @GetMapping("/{hubId}")
  ResponseEntity<HubInfoResponseDto> getHubInfo(@PathVariable("hubId") UUID hubId);
}
