package com.bulbas23r.client.hub.hub.presentation.controller;

import com.bulbas23r.client.hub.hub.application.service.HubService;
import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.hub.presentation.dto.request.CreateHubRequestDto;
import com.bulbas23r.client.hub.hub.presentation.dto.request.UpdateHubRequestDto;
import com.bulbas23r.client.hub.hub.presentation.dto.response.HubListResponseDto;
import com.bulbas23r.client.hub.hub.presentation.dto.response.HubResponseDto;
import common.annotation.RoleCheck;
import common.model.UserRoleEnum.Authority;
import common.utils.PageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hubs")
@RequiredArgsConstructor
@Tag(name = "Hub", description = "허브 관련 API")
public class HubController {

    private final HubService hubService;

    @PostMapping
    @RoleCheck(Authority.MASTER)
    @Operation(summary = "허브 생성하기")
    public ResponseEntity<HubResponseDto> createHub(
        @RequestBody @Valid CreateHubRequestDto requestDto) {
        Hub hub = hubService.createHub(requestDto);

        return ResponseEntity.ok(new HubResponseDto(hub));
    }

    @GetMapping("/{hubId}")
    @Operation(summary = "허브 단건 조회하기")
    public ResponseEntity<HubResponseDto> getHub(@PathVariable UUID hubId) {
        Hub hub = hubService.getHubById(hubId);

        return ResponseEntity.ok(new HubResponseDto(hub));
    }

    @GetMapping
    @Operation(summary = "허브 리스트 조회하기")
    public ResponseEntity<Page<HubListResponseDto>> getHubList(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<Hub> hubs = hubService.getHubList(pageable);

        return ResponseEntity.ok(hubs.map(HubListResponseDto::new));
    }

    @PutMapping("/{hubId}")
    @RoleCheck(Authority.MASTER)
    @Operation(summary = "허브 수정하기")
    public ResponseEntity<HubResponseDto> updateHub(
        @PathVariable UUID hubId,
        @RequestBody @Valid UpdateHubRequestDto requestDto
    ) {
        Hub hub = hubService.updateHub(hubId, requestDto);

        return ResponseEntity.ok(new HubResponseDto(hub));
    }

    @DeleteMapping("/{hubId}")
    @RoleCheck(Authority.MASTER)
    @Operation(summary = "허브 삭제하기")
    public ResponseEntity<Void> deleteHub(@PathVariable UUID hubId) {
        hubService.deleteHub(hubId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "허브 검색하기")
    public ResponseEntity<Page<HubListResponseDto>> searchHub(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "DESC", required = false) Sort.Direction sortDirection,
        @RequestParam(defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy,
        @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<Hub> hubList = hubService.searchHub(pageable, sortDirection, sortBy, keyword);

        return ResponseEntity.ok(hubList.map(HubListResponseDto::new));
    }
}
