package com.bulbas23r.client.product.infrastructure.client;

import com.bulbas23r.client.product.presentation.dto.ProductHubResponseDto;
import common.config.FeignConfig;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service", url = "http://localhost:19091/api/hubs", configuration = FeignConfig.class)
public interface HubClient {
    @GetMapping("/{hubId}")
    ProductHubResponseDto getHub(@PathVariable UUID hubId);
}
