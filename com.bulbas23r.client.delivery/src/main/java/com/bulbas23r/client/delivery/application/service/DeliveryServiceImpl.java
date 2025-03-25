package com.bulbas23r.client.delivery.application.service;

import com.bulbas23r.client.delivery.domain.model.Delivery;
import com.bulbas23r.client.delivery.domain.model.DeliveryRoute;
import com.bulbas23r.client.delivery.domain.model.DeliveryStatus;
import com.bulbas23r.client.delivery.domain.repository.DeliveryQueryRepository;
import com.bulbas23r.client.delivery.domain.repository.DeliveryRepository;
import com.bulbas23r.client.delivery.infrastructure.client.CompanyClient;
import com.bulbas23r.client.delivery.infrastructure.client.HubClient;
import com.bulbas23r.client.delivery.infrastructure.client.UserClient;
import com.bulbas23r.client.delivery.infrastructure.persistence.DeliveryRouteJpaRepository;
import com.bulbas23r.client.delivery.presentation.dto.request.DeliveryCreateRequestDto;
import com.bulbas23r.client.delivery.presentation.dto.request.DeliverySearchRequestDto;
import com.bulbas23r.client.delivery.presentation.dto.request.DeliveryUpdateRequestDto;
import com.bulbas23r.client.delivery.presentation.dto.response.DeliveryResponseDto;
import com.bulbas23r.client.delivery.presentation.dto.response.HubRouteResponseDto;
import common.dto.CompanyInfoResponseDto;
import common.dto.UserInfoResponseDto;
import common.event.CreateOrderEventDto;
import common.exception.BadRequestException;
import common.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryQueryRepository deliveryQueryRepository;
    private final DeliveryRouteJpaRepository deliveryRouteJpaRepository;
    private final HubClient hubClient;
    private final CompanyClient companyClient;
    private final UserClient userClient;

    @Transactional
    public void createDeliveryByOrder(CreateOrderEventDto eventDto) {
        CompanyInfoResponseDto providerCompanyInfoDto = companyClient.getCompanyInfoById(
            eventDto.getProvideCompanyId()).getBody();
        CompanyInfoResponseDto receiverCompanyInfoDto = companyClient.getCompanyInfoById(
            eventDto.getReceiverCompanyId()).getBody();

        UserInfoResponseDto userInfoResponseDto = userClient.getUserInfo(
                receiverCompanyInfoDto.getManagerId())
            .getBody();

        DeliveryCreateRequestDto requestDto = DeliveryCreateRequestDto.builder()
            .orderId(eventDto.getOrderId())
            .departureHubId(providerCompanyInfoDto.getHubId())
            .arrivalHubId(receiverCompanyInfoDto.getHubId())
            .receiverCompanyId(eventDto.getReceiverCompanyId())
            .receiverCompanySlackId(userInfoResponseDto.getSlackId())
            .build();

        createDelivery(requestDto);
    }


    @Override
    @Transactional
    public DeliveryResponseDto createDelivery(DeliveryCreateRequestDto requestDto) {

        Delivery delivery = requestDto.toDelivery();
        deliveryRepository.save(delivery);

        //배송 경로 생성
        List<DeliveryRoute> routeList = createDeliveryRoute(requestDto.getDepartureHubId(),
            requestDto.getArrivalHubId(), delivery);
        delivery.setDeliveryRouteList(routeList);
        deliveryRouteJpaRepository.saveAll(routeList);

        return DeliveryResponseDto.fromEntity(delivery);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryResponseDto getDelivery(UUID deliveryId) {
        return DeliveryResponseDto.fromEntity(findById(deliveryId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryResponseDto> getDeliveryList(Pageable pageable) {
        return deliveryRepository.findAll(pageable).map(DeliveryResponseDto::fromEntity);
    }

    @Override
    @Transactional
    public DeliveryResponseDto updateDelivery(UUID id, DeliveryUpdateRequestDto requestDto) {
        Delivery delivery = findById(id);

        if (!delivery.getStatus().equals(DeliveryStatus.READY)) {
            throw new BadRequestException("배송 전에만 수정 할 수 있습니다.");
        }
        //todo: 출발지, 도착지 변경 시, 배송 경로 수정 로직 추가
        delivery.update(requestDto);

        return DeliveryResponseDto.fromEntity(delivery);
    }

    @Override
    @Transactional
    public DeliveryResponseDto deleteDelivery(UUID deliveryId) {
        Delivery delivery = findById(deliveryId);

        if (!delivery.getStatus().equals(DeliveryStatus.READY)) {
            throw new BadRequestException("배송 전에만 삭제 할 수 있습니다.");
        }

        delivery.setDeleted(true);
        delivery.getDeliveryRouteList().forEach(DeliveryRoute::setDeleted);

        return DeliveryResponseDto.fromEntity(delivery);
    }

    @Override
    public Page<DeliveryResponseDto> searchDelivery(DeliverySearchRequestDto requestDto) {
        return deliveryQueryRepository.searchDelivery(
            requestDto.getOrderId(),
            requestDto.getStartHubId(),
            requestDto.getEndHubId(),
            requestDto.getDeliveryManagerId(),
            requestDto.getReceiverCompanyId(),
            requestDto.toPageable(),
            requestDto.getSortDirection(),
            requestDto.getSortBy()
        );
    }

    @Transactional
    public void changeStatus(UUID deliveryId, DeliveryStatus status) {
        Delivery delivery = findById(deliveryId);
        delivery.changeStatus(status);
    }

    @Override
    @Transactional
    public void cancelDelivery(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);

        delivery.changeStatus(DeliveryStatus.CANCELED);
    }

    @Override
    @Transactional
    public Delivery getDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId).orElseThrow(
            () -> new BadRequestException("유효하지 않은 주문 아이디입니다!")
        );
    }

    public Delivery findById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new NotFoundException("배송 정보를 찾을 수 없습니다."));
    }


    public List<DeliveryRoute> createDeliveryRoute(UUID departureHubId, UUID arrivalHubId,
        Delivery delivery) {
        List<DeliveryRoute> deliveryRouteList = new ArrayList<>();

        List<UUID> routeList = hubClient.getHubShortRouteList(departureHubId, arrivalHubId);

        for (int i = 1; i < routeList.size(); i++) {

            HubRouteResponseDto hubRoute = hubClient.getHubRoute(routeList.get(i - 1),
                routeList.get(i));

            deliveryRouteList.add(
                DeliveryRoute.builder()
                    .delivery(delivery)
                    .departureHubId(routeList.get(i - 1))
                    .arrivalHubId(routeList.get(i))
                    .estimatedDistance(hubRoute.getTransitDistance())
                    .estimatedDuration(hubRoute.getTransitTime())
                    .sequence(i)
                    .build()
            );
        }

        return deliveryRouteList;
    }
}
