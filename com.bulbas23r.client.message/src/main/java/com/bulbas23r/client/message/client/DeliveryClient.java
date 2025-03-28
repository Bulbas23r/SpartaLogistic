package com.bulbas23r.client.message.client;

import com.bulbas23r.client.message.presentation.dto.response.DeliveryResponseDto;
import common.config.FeignConfig;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "delivery-service", url = "http://localhost:19091/api/deliveries", configuration = FeignConfig.class)
@FeignClient(name = "delivery-service", url = "${delivery-service.url}", configuration = FeignConfig.class)
public interface DeliveryClient {
  @GetMapping("/{deliveryId}/route")
  ResponseEntity<Page<DeliveryResponseDto>> getDeliveryRouteList(
      @PathVariable("deliveryId") UUID deliveryId
  );

  @GetMapping("/order/{orderId}")
  ResponseEntity<DeliveryResponseDto> getDeliveryByOrderId(
      @PathVariable("orderId") UUID orderId
  );
}
