package com.bulbas23r.client.delivery.presentation.controller;

import com.bulbas23r.client.delivery.application.dto.DeliveryRouteArriveRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryRouteCreateRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryRouteDepartRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryRouteResponseDto;
import com.bulbas23r.client.delivery.application.service.DeliveryRouteService;
import com.bulbas23r.client.delivery.domain.model.DeliveryRoute;
import common.annotation.RoleCheck;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryRouteController {

    private final DeliveryRouteService deliveryRouteService;


    @RoleCheck({"MASTER", "HUB_MANAGER", "HUB_TO_HUB_DELIVERY"})
    @PostMapping("/route/depart")
    public ResponseEntity<?> departDeliveryRoute(@RequestBody @Valid DeliveryRouteDepartRequestDto requestDto) {

        DeliveryRouteResponseDto result = deliveryRouteService.departDeliveryRoute(requestDto);

        return ResponseEntity.ok(result);
    }

    @RoleCheck({"MASTER", "HUB_MANAGER", "HUB_TO_HUB_DELIVERY"})
    @PostMapping("/route/arrive")
    public ResponseEntity<?> arriveDeliveryRoute(@RequestBody @Valid DeliveryRouteArriveRequestDto requestDto) {

        DeliveryRouteResponseDto result = deliveryRouteService.arriveDeliveryRoute(requestDto);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{deliveryId}/route")
    public ResponseEntity<?> getDeliveryRouteList(
        @PathVariable UUID deliveryId,
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue =  "10", required = false) int size) {

        Pageable pageable = PageRequest.of(page, size, Direction.ASC, "sequence");
        Page<DeliveryRouteResponseDto> result = deliveryRouteService.getDeliveryRouteList(deliveryId,pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{orderId}/order/route")
    public ResponseEntity<?> getDeliveryByOrderIdRouteList(
        @PathVariable UUID orderId,
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue =  "10", required = false) int size) {

        Pageable pageable = PageRequest.of(page, size, Direction.ASC, "sequence");
        Page<DeliveryRouteResponseDto> result = deliveryRouteService.getDeliveryByOrderIdRouteList(orderId,pageable);

        return ResponseEntity.ok(result);
    }

}
