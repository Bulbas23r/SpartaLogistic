package com.bulbas23r.client.deliverymanager.presentation.controller;

import com.bulbas23r.client.deliverymanager.application.service.DeliveryManagerService;
import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.presentation.dto.CreateDeliveryManagerRequestDto;
import com.bulbas23r.client.deliverymanager.presentation.dto.DeliveryManagerResponse;
import com.bulbas23r.client.deliverymanager.presentation.dto.UpdateDeliveryManagerRequestDto;
import common.utils.PageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Delivery Manager", description = "배송 담당자 관련 API")
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    @PostMapping
    @Operation(summary = "배송 담당자 생성하기")
    public ResponseEntity<DeliveryManagerResponse> crateDeliveryManager(
        @RequestBody CreateDeliveryManagerRequestDto requestDto) {
        DeliveryManager deliveryManager = deliveryManagerService.createDeliveryManager(requestDto);

        return ResponseEntity.ok(new DeliveryManagerResponse(deliveryManager));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "배송 담당자 단건 조회")
    public ResponseEntity<DeliveryManagerResponse> getDeliveryManager(
        @PathVariable Long userId
    ) {
        DeliveryManager deliveryManager = deliveryManagerService.getDeliveryManager(userId);

        return ResponseEntity.ok(new DeliveryManagerResponse(deliveryManager));
    }

    @GetMapping
    @Operation(summary = "배송 담당자 리스트 조회")
    public ResponseEntity<Page<DeliveryManagerResponse>> getDeliveryManagerList(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<DeliveryManager> deliveryManagerList = deliveryManagerService
            .getDeliveryManagerList(pageable);

        return ResponseEntity.ok(deliveryManagerList.map(DeliveryManagerResponse::new));
    }

    @DeleteMapping("/userId")
    @Operation(summary = "배송 담당자 삭제하기")
    public ResponseEntity<Void> deleteDeliveryManager(@RequestParam Long userId) {
        deliveryManagerService.deleteDeliveryManager(userId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/userId")
    @Operation(summary = "배송 담당자 수정하기")
    public ResponseEntity<DeliveryManagerResponse> updateDeliveryManager(
        @RequestParam Long userId,
        @RequestBody @Valid UpdateDeliveryManagerRequestDto requestDto) {
        DeliveryManager deliveryManager = deliveryManagerService.updateDeliveryManager(userId,
            requestDto);

        return ResponseEntity.ok(new DeliveryManagerResponse(deliveryManager));
    }

    @GetMapping("/search")
    @Operation(summary = "배송 담당자 검색하기")
    public ResponseEntity<Page<DeliveryManagerResponse>> searchDeliveryManager(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "DESC", required = false) Sort.Direction sortDirection,
        @RequestParam(defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy,
        @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<DeliveryManager> deliveryManagers =
            deliveryManagerService.searchDeliveryManagerList(pageable, sortDirection,
                sortBy, keyword);

        return ResponseEntity.ok(deliveryManagers.map(DeliveryManagerResponse::new));
    }
}
