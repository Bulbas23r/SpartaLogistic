package com.bulbas23r.client.hub.hub.presentation.controller;

import com.bulbas23r.client.hub.hub.application.service.HubService;
import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.hub.presentation.dto.CreateHubRequestDto;
import com.bulbas23r.client.hub.hub.presentation.dto.HubResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
