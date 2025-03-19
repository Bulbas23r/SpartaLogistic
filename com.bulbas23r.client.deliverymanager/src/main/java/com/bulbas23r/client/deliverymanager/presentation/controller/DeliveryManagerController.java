package com.bulbas23r.client.deliverymanager.presentation.controller;

import com.bulbas23r.client.deliverymanager.application.service.DeliveryManagerService;
import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.presentation.dto.CreateDeliveryManagerRequestDto;
import com.bulbas23r.client.deliverymanager.presentation.dto.DeliveryManagerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    @PostMapping
    public ResponseEntity<DeliveryManagerResponse> crateDeliveryManager(
        @RequestBody CreateDeliveryManagerRequestDto requestDto) {
        DeliveryManager deliveryManager = deliveryManagerService.createDeliveryManager(requestDto);

        return ResponseEntity.ok(new DeliveryManagerResponse(deliveryManager));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<DeliveryManagerResponse> getDeliveryManager(
        @PathVariable Long userId
    ) {
        DeliveryManager deliveryManager = deliveryManagerService.getDeliveryManager(userId);

        return ResponseEntity.ok(new DeliveryManagerResponse(deliveryManager));
    }
}
