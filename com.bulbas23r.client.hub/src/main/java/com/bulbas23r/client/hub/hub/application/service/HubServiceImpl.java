package com.bulbas23r.client.hub.hub.application.service;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.hub.domain.repository.HubRepository;
import com.bulbas23r.client.hub.hub.presentation.dto.CreateHubRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {

    private final HubRepository hubRepository;

    @Override
    public Hub createHub(CreateHubRequestDto requestDto) {
        // TODO 허브 당당자 ID 검증 로직 추가하기
        Hub hub = new Hub(requestDto);

        return hubRepository.save(hub);
    }
}
