package com.bulbas23r.client.hub.hub.application.service;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.hub.presentation.dto.request.CreateHubRequestDto;
import com.bulbas23r.client.hub.hub.presentation.dto.request.UpdateHubRequestDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubService {

    Hub createHub(CreateHubRequestDto requestDto);

    Hub updateHub(UUID hubId, UpdateHubRequestDto requestDto);

    Hub getHubById(UUID hubId);

    Page<Hub> getHubList(Pageable pageable);
}
