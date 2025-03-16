package com.bulbas23r.client.hub.hub.application.service;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.hub.presentation.dto.CreateHubRequestDto;
import com.bulbas23r.client.hub.hub.presentation.dto.UpdateHubRequestDto;
import java.util.UUID;

public interface HubService {

    Hub createHub(CreateHubRequestDto requestDto);

    Hub updateHub(UUID hubId, UpdateHubRequestDto requestDto);

    Hub getHubById(UUID hubId);
}
