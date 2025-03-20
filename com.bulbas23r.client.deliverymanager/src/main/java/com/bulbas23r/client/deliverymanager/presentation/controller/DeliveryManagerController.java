package com.bulbas23r.client.deliverymanager.presentation.controller;

import com.bulbas23r.client.deliverymanager.application.service.DeliveryManagerService;
import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.presentation.dto.CreateDeliveryManagerRequestDto;
import com.bulbas23r.client.deliverymanager.presentation.dto.DeliveryManagerResponse;
import com.bulbas23r.client.deliverymanager.presentation.dto.UpdateDeliveryManagerRequestDto;
import common.annotation.RoleCheck;
import common.exception.BadRequestException;
import common.header.UserInfoHeader;
import common.model.UserRoleEnum.Authority;
import common.utils.PageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Delivery Manager", description = "배송 담당자 관련 API")
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    @PostMapping
    @RoleCheck({Authority.MASTER, Authority.HUB_MANAGER})
    @Operation(summary = "배송 담당자 생성하기")
    public ResponseEntity<DeliveryManagerResponse> crateDeliveryManager(
        @RequestBody @Valid CreateDeliveryManagerRequestDto requestDto) {
        // TODO 허브 담당자 검증
        DeliveryManager deliveryManager = deliveryManagerService.createDeliveryManager(requestDto);

        return ResponseEntity.ok(new DeliveryManagerResponse(deliveryManager));
    }

    @GetMapping("/{userId}")
    @RoleCheck({Authority.MASTER, Authority.HUB_MANAGER, Authority.HUB_TO_HUB_DELIVERY,
        Authority.TO_COMPANY_DELIVERY})
    @Operation(summary = "배송 담당자 단건 조회")
    public ResponseEntity<DeliveryManagerResponse> getDeliveryManager(
        @PathVariable Long userId,
        @RequestHeader(UserInfoHeader.USER_ROLE) String requestUserRole,
        @RequestHeader(UserInfoHeader.USER_ID) Long requestUserId
    ) {
        // TODO 허브 담당자 검증
        DeliveryManager deliveryManager = deliveryManagerService.getDeliveryManager(userId);

        if (requestUserRole.equals(Authority.HUB_TO_HUB_DELIVERY) ||
            requestUserRole.equals(Authority.TO_COMPANY_DELIVERY)) {
            if (!deliveryManagerService.checkDeliveryManager(requestUserId, deliveryManager)) {
                throw new BadRequestException("본인 정보만 조회할 수 있습니다!");
            }
        }

        return ResponseEntity.ok(new DeliveryManagerResponse(deliveryManager));
    }

    @GetMapping
    @RoleCheck(Authority.MASTER)
    @Operation(summary = "모든 배송 담당자 리스트 조회")
    public ResponseEntity<Page<DeliveryManagerResponse>> getDeliveryManagerList(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<DeliveryManager> deliveryManagerList = deliveryManagerService
            .getDeliveryManagerList(pageable);

        return ResponseEntity.ok(deliveryManagerList.map(DeliveryManagerResponse::new));
    }

    @GetMapping("/hub")
    @RoleCheck(Authority.MASTER)
    @Operation(summary = "허브 배송 담당자 리스트 조회")
    public ResponseEntity<List<DeliveryManagerResponse>> getHubDeliveryManagerList() {
        List<DeliveryManager> deliveryManagerList = deliveryManagerService.getHubDeliveryManagerList();

        return ResponseEntity.ok(
            deliveryManagerList.stream().map(DeliveryManagerResponse::new).toList());
    }

    @GetMapping("/company/{hubId}")
    @RoleCheck({Authority.MASTER, Authority.HUB_MANAGER})
    @Operation(summary = "업체 배송 담당자 리스트 조회")
    public ResponseEntity<List<DeliveryManagerResponse>> getCompanyDeliveryManagerList(
        @PathVariable UUID hubId) {
        // TODO 허브 담당자 검증
        List<DeliveryManager> deliveryManagerList =
            deliveryManagerService.getCompanyDeliveryManagerList(hubId);

        return ResponseEntity.ok(
            deliveryManagerList.stream().map(DeliveryManagerResponse::new).toList());
    }

    @DeleteMapping("/userId")
    @RoleCheck({Authority.MASTER, Authority.HUB_MANAGER})
    @Operation(summary = "배송 담당자 삭제하기")
    public ResponseEntity<Void> deleteDeliveryManager(@RequestParam Long userId) {
        // TODO 허브 담당자 검증
        deliveryManagerService.deleteDeliveryManager(userId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/userId")
    @RoleCheck({Authority.MASTER, Authority.HUB_MANAGER})
    @Operation(summary = "배송 담당자 수정하기")
    public ResponseEntity<DeliveryManagerResponse> updateDeliveryManager(
        @RequestParam Long userId,
        @RequestBody @Valid UpdateDeliveryManagerRequestDto requestDto) {
        // TODO 허브 담당자 검증
        DeliveryManager deliveryManager = deliveryManagerService.updateDeliveryManager(userId,
            requestDto);

        return ResponseEntity.ok(new DeliveryManagerResponse(deliveryManager));
    }

    @GetMapping("/search")
    @RoleCheck({Authority.MASTER, Authority.HUB_MANAGER})
    @Operation(summary = "배송 담당자 검색하기")
    public ResponseEntity<Page<DeliveryManagerResponse>> searchDeliveryManager(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "DESC", required = false) Sort.Direction sortDirection,
        @RequestParam(defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy,
        @RequestParam(required = false) String keyword
    ) {
        // TODO 허브 담당자 검증
        Pageable pageable = PageUtils.pageable(page, size);
        Page<DeliveryManager> deliveryManagers =
            deliveryManagerService.searchDeliveryManagerList(pageable, sortDirection,
                sortBy, keyword);

        return ResponseEntity.ok(deliveryManagers.map(DeliveryManagerResponse::new));
    }
}
