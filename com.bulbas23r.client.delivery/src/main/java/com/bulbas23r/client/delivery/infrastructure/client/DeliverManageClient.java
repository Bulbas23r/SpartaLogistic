package com.bulbas23r.client.delivery.infrastructure.client;

import com.bulbas23r.client.delivery.presentation.dto.response.DeliveryManagerResponseDto;
import common.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "delivery-manager-service", url = "http://localhost:19091/api/managers",configuration = FeignConfig.class)
public interface DeliverManageClient {

    @GetMapping("/{userId}")
    DeliveryManagerResponseDto getDeliveryManagerByUserId(@PathVariable("userId") Long userId);
}
