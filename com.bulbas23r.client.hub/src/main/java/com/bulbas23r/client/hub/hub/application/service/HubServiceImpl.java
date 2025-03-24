package com.bulbas23r.client.hub.hub.application.service;

import com.bulbas23r.client.hub.global.cache.CacheName;
import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.hub.domain.repository.HubQueryRepository;
import com.bulbas23r.client.hub.hub.domain.repository.HubRepository;
import com.bulbas23r.client.hub.hub.infrastructure.client.UserClient;
import com.bulbas23r.client.hub.hub.presentation.dto.request.CreateHubRequestDto;
import com.bulbas23r.client.hub.hub.presentation.dto.request.UpdateHubRequestDto;
import com.bulbas23r.client.hub.route.domain.model.Route;
import com.bulbas23r.client.hub.route.domain.repository.RouteRepository;
import com.bulbas23r.client.hub.stock.domain.model.Stock;
import com.bulbas23r.client.hub.stock.domain.repository.StockRepository;
import com.bulbas23r.client.hub.stock.infrastructure.messaging.HubEventProducer;
import common.dto.UserInfoResponseDto;
import common.exception.BadRequestException;
import common.exception.NotFoundException;
import common.model.UserRoleEnum;
import common.utils.PageUtils.CommonSortBy;
import java.util.List;
import java.util.Objects;
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
    private final UserClient userClient;
    private final RouteRepository routeRepository;
    private final StockRepository stockRepository;
    private final HubEventProducer hubEventProducer;

    @Override
    @Transactional
    @CachePut(cacheNames = CacheName.HUB, key = "#result.id")
    public Hub createHub(CreateHubRequestDto requestDto) {
        if (hubRepository.existsByName(requestDto.getName())) {
            throw new BadRequestException("해당 허브 이름이 이미 존재합니다!");
        }

        if (Objects.nonNull(requestDto.getManagerId())) {
            UserInfoResponseDto userInfo = userClient.getUserInfo(requestDto.getManagerId())
                .getBody();

            if (userInfo == null || !userInfo.getRole().equals(UserRoleEnum.HUB_MANAGER)) {
                throw new BadRequestException("유저가 존재하지 않거나 허브 관리자가 아닙니다!");
            }
        }

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
        cacheNames = CacheName.HUB_LIST,
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
        @CacheEvict(cacheNames = CacheName.HUB_LIST, allEntries = true),
        @CacheEvict(cacheNames = CacheName.HUB_SEARCH, allEntries = true)
    }, put = {
        @CachePut(cacheNames = CacheName.HUB, key = "#result.id")
    })
    public Hub updateHub(UUID hubId, UpdateHubRequestDto requestDto) {
        if (Objects.nonNull(requestDto.getManagerId())) {
            UserInfoResponseDto userInfo = userClient.getUserInfo(requestDto.getManagerId())
                .getBody();

            if (userInfo == null || !userInfo.getRole().equals(UserRoleEnum.HUB_MANAGER)) {
                throw new BadRequestException("유저가 존재하지 않거나 허브 관리자가 아닙니다!");
            }
        }

        Hub hub = getHubById(hubId);
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
        Hub hub = getHubById(hubId);
        hub.setDeleted(true);
        hubEventProducer.sendDeleteHubEvent(hub.getId());
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

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheName.HUB, key = "#managerId")
    public Hub getHubByManagerId(Long managerId) {
        return hubRepository.findByManagerId(managerId).orElseThrow(
            () -> new BadRequestException("해당 유저가 담당중인 허브가 존재하지 않습니다!")
        );
    }


    @Override
    @Transactional
    public void deleteStocksByProductId(UUID productId) {
        List<Stock> stockList = stockRepository.findAllByProductId(productId);

        stockList.forEach(Stock::setDeleted);
    }

    @Override
    @Transactional
    public void deleteRoutesByHubId(UUID hubId) {
        List<Route> routeList = routeRepository.findAllByHubId(hubId);

        routeList.forEach(Route::setDeleted);
    }
}
