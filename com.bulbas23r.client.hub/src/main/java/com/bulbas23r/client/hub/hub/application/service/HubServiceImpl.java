package com.bulbas23r.client.hub.hub.application.service;

import com.bulbas23r.client.hub.global.cache.CacheName;
import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.hub.domain.repository.HubQueryRepository;
import com.bulbas23r.client.hub.hub.domain.repository.HubRepository;
import com.bulbas23r.client.hub.hub.presentation.dto.request.CreateHubRequestDto;
import com.bulbas23r.client.hub.hub.presentation.dto.request.UpdateHubRequestDto;
import common.exception.NotFoundException;
import common.utils.PageUtils.CommonSortBy;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {

    private final HubRepository hubRepository;
    private final HubQueryRepository hubQueryRepository;

    @Resource(name = "hubServiceImpl")
    HubService self;

    @Override
    @Transactional
    @CachePut(cacheNames = CacheName.HUB, key = "result.id")
    public Hub createHub(CreateHubRequestDto requestDto) {
        // TODO 허브 당당자 ID 검증 로직 추가하기
        Hub hub = new Hub(requestDto);

        return hubRepository.save(hub);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheName.HUB, key = "#hubId")
    public Hub getHubById(UUID hubId) {
        return hubRepository.findById(hubId).orElseThrow(
            () -> new NotFoundException("존재하지 않는 허브 ID 입니다.")
        );
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
        cacheNames = CacheName.HUB_SEARCH,
        key = "{ #pageable.pageNumber, #pageable.pageSize }"
    )
    public Page<Hub> getHubList(Pageable pageable) {
        return hubRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheName.HUB_LIST, key = "#root.methodName")
    public List<Hub> getActiveHubList() {
        return hubRepository.findByActiveTrue();
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = CacheName.HUB_LIST, allEntries = true)
        @CacheEvict(cacheNames = CacheName.HUB_SEARCH, allEntries = true)
    }, put = {
        @CachePut(cacheNames = CacheName.HUB, key = "#result.id")
    })
    public Hub updateHub(UUID hubId, UpdateHubRequestDto requestDto) {
        // TODO 허브 당당자 ID 검증 로직 추가하기

        Hub hub = self.getHubById(hubId);
        hub.update(requestDto);

        return hub;
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = CacheName.HUB, key = "#hubId"),
        @CacheEvict(cacheNames = CacheName.HUB_LIST, allEntries = true),
        @CacheEvict(cacheNames = CacheName.HUB_SEARCH, allEntries = true)
    })
    public void deleteHub(UUID hubId) {
        Hub hub = self.getHubById(hubId);
        hub.setDeleted(true);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
        cacheNames = CacheName.HUB_SEARCH,
        key = "{ #pageable.pageNumber, #pageable.pageSize, #sortDirection, #sortBy, #keyword }"
    )
    public Page<Hub> searchHub(Pageable pageable, Direction sortDirection, CommonSortBy sortBy,
        String keyword) {
        return hubQueryRepository.searchHub(pageable, sortDirection, sortBy, keyword);
    }
}
