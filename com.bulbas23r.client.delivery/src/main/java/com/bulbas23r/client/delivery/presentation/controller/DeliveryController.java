package com.bulbas23r.client.delivery.presentation.controller;

import com.bulbas23r.client.delivery.application.dto.DeliveryCreateRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryResponseDto;
import com.bulbas23r.client.delivery.application.dto.DeliverySearchRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryUpdateRequestDto;
import com.bulbas23r.client.delivery.application.service.DeliveryService;
import common.annotation.RoleCheck;
import common.utils.PageUtils;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

//    @RoleCheck({"MASTER", "HUB_MANAGER"})
    @PostMapping
    public ResponseEntity<?> createDelivery(@Valid @RequestBody DeliveryCreateRequestDto requestDto) {
        System.out.println("!!!!!");
        DeliveryResponseDto result = deliveryService.createDelivery(requestDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<?> getDelivery(@PathVariable("deliveryId") UUID deliveryId) {
        DeliveryResponseDto result = deliveryService.getDelivery(deliveryId);
        return ResponseEntity.ok(result);
    }


    @GetMapping
    public ResponseEntity<?> getDeliveryList(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name ="size",defaultValue =  "10", required = false) int size
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<DeliveryResponseDto> result = deliveryService.getDeliveryList(pageable);
        return ResponseEntity.ok(result);
    }

    @RoleCheck({"MASTER", "HUB_MANAGER"})
    @PutMapping("/{deliveryId}")
    public ResponseEntity<?> updateDelivery(
        @PathVariable("deliveryId") UUID deliveryId,
        @Valid @RequestBody DeliveryUpdateRequestDto requestDto) {
        DeliveryResponseDto result = deliveryService.updateDelivery(deliveryId,requestDto);

        return ResponseEntity.ok(result);
    }

    @RoleCheck({"MASTER", "HUB_MANAGER"})
    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<?> deleteDelivery(@PathVariable("deliveryId") UUID deliveryId) {
        DeliveryResponseDto result = deliveryService.deleteDelivery(deliveryId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchDelivery(@ModelAttribute DeliverySearchRequestDto requestDto) {
        Page<DeliveryResponseDto> result = deliveryService.searchDelivery(requestDto);
        return ResponseEntity.ok(result);
    }



}
