package com.bulbas23r.client.hub.hub.application.service;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.hub.presentation.dto.CreateHubRequestDto;

public interface HubService {

    Hub createHub(CreateHubRequestDto requestDto);
}
