package com.bulbas23r.client.hub.hub.presentation.controller;

import com.bulbas23r.client.hub.hub.application.service.HubService;
import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.hub.presentation.dto.request.CreateHubRequestDto;
import com.bulbas23r.client.hub.hub.presentation.dto.request.UpdateHubRequestDto;
import com.bulbas23r.client.hub.hub.presentation.dto.response.HubListResponseDto;
import com.bulbas23r.client.hub.hub.presentation.dto.response.HubResponseDto;
import common.utils.PageUtils;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
public class HubController {

    private final HubService hubService;

    @PostMapping
    public ResponseEntity<HubResponseDto> createHub(
        @RequestBody @Valid CreateHubRequestDto requestDto) {
        Hub hub = hubService.createHub(requestDto);

        return ResponseEntity.ok(new HubResponseDto(hub));
    }

    @GetMapping("/{hubId}")
    public ResponseEntity<HubResponseDto> getHub(@PathVariable UUID hubId) {
        Hub hub = hubService.getHubById(hubId);

        return ResponseEntity.ok(new HubResponseDto(hub));
    }

    @GetMapping
    public ResponseEntity<Page<HubListResponseDto>> getHubList(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<Hub> hubs = hubService.getHubList(pageable);

        return ResponseEntity.ok(hubs.map(HubListResponseDto::new));
    }

    @PutMapping("/{hubId}")
    public ResponseEntity<HubResponseDto> updateHub(
        @PathVariable UUID hubId,
        @RequestBody @Valid UpdateHubRequestDto requestDto
    ) {
        Hub hub = hubService.updateHub(hubId, requestDto);

        return ResponseEntity.ok(new HubResponseDto(hub));
    }

}
