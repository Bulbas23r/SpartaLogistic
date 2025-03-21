package com.bulbas23r.client.delivery.presentation.controller;

import com.bulbas23r.client.delivery.application.dto.DeliveryRouteCreateRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryRouteArriveRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryRouteResponseDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryRouteDepartRequestDto;
import com.bulbas23r.client.delivery.application.service.DeliveryRouteService;
import com.bulbas23r.client.delivery.domain.model.DeliveryRoute;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries/route")
public class DeliveryRouteController {

    private final DeliveryRouteService deliveryRouteService;

    // 배송 경로 임시 생성 -> 삭제 예정
    @PostMapping
    public ResponseEntity<?> deliveryPath(@RequestBody DeliveryRouteCreateRequestDto requestDto) {

        DeliveryRoute result = deliveryRouteService.createDeliveryRoute(requestDto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/depart")
    public ResponseEntity<?> departDeliveryRoute(@RequestBody @Valid DeliveryRouteDepartRequestDto requestDto) {

        DeliveryRouteResponseDto result = deliveryRouteService.departDeliveryRoute(requestDto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/arrive")
    public ResponseEntity<?> arriveDeliveryRoute(@RequestBody @Valid DeliveryRouteArriveRequestDto requestDto) {

        DeliveryRouteResponseDto result = deliveryRouteService.arriveDeliveryRoute(requestDto);

        return ResponseEntity.ok(result);
    }

}
