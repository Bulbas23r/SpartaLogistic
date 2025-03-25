package com.bulbas23r.client.message.client;

import com.bulbas23r.client.message.presentation.dto.OrderReseponseDto;
import common.config.FeignConfig;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "order-service", url = "http://localhost:19091/api/orders", configuration = FeignConfig.class)
@FeignClient(name = "order-service", url = "${order-service.url}", configuration = FeignConfig.class)
public interface OrderClient {
  @GetMapping("/{orderId}")
  ResponseEntity<OrderReseponseDto> getOrder(@PathVariable("orderId") UUID orderId);
}
