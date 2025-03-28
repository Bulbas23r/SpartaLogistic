package com.bulbas23r.client.message.client;

import com.bulbas23r.client.message.presentation.dto.ProductResponseDto;
import common.config.FeignConfig;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${product-service.url}", configuration = FeignConfig.class)
public interface ProductClient {
  @GetMapping("/{productId}")
  ResponseEntity<ProductResponseDto> getProduct(@PathVariable("productId") UUID productId);
}
