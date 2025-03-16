package com.bulbas23r.client.hub.hub.application.service;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.hub.domain.repository.HubRepository;
import com.bulbas23r.client.hub.hub.presentation.dto.request.CreateHubRequestDto;
import com.bulbas23r.client.hub.hub.presentation.dto.request.UpdateHubRequestDto;
import common.exception.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {

    private final HubRepository hubRepository;

    @Transactional
    @Override
    public Hub createHub(CreateHubRequestDto requestDto) {
        // TODO 허브 당당자 ID 검증 로직 추가하기
        Hub hub = new Hub(requestDto);

        return hubRepository.save(hub);
    }

    @Transactional(readOnly = true)
    @Override
    public Hub getHubById(UUID hubId) {
        return hubRepository.findById(hubId).orElseThrow(
            () -> new NotFoundException("존재하지 않는 허브 ID 입니다.")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Hub> getHubList(Pageable pageable) {
        return hubRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Hub updateHub(UUID hubId, UpdateHubRequestDto requestDto) {
        // TODO 허브 당당자 ID 검증 로직 추가하기

        Hub hub = getHubById(hubId);
        hub.update(requestDto);

        return hub;
    }
}
