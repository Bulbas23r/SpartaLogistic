package com.bulbas23r.client.delivery.application.service;

import com.bulbas23r.client.delivery.application.dto.DeliveryRouteCreateRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryRouteArriveRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryRouteResponseDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryRouteDepartRequestDto;
import com.bulbas23r.client.delivery.domain.model.Delivery;
import com.bulbas23r.client.delivery.domain.model.DeliveryRoute;
import com.bulbas23r.client.delivery.domain.model.DeliveryRouteStatus;
import com.bulbas23r.client.delivery.domain.model.DeliveryStatus;
import com.bulbas23r.client.delivery.domain.repository.DeliveryRouteRepository;
import common.exception.BadRequestException;
import common.exception.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryRouteServiceImpl implements DeliveryRouteService {

    private final DeliveryRouteRepository deliveryRouteRepository;
    private final DeliveryService deliveryService;
    private final DeliveryServiceImpl deliveryServiceImpl;

    @Override
    @Transactional
    public DeliveryRoute createDeliveryRoute(DeliveryRouteCreateRequestDto requestDto) {

        Delivery delivery = deliveryServiceImpl.findById(requestDto.getDeliveryId());

        DeliveryRoute deliveryRoute = requestDto.toDeliveryRoute(delivery);

        deliveryRouteRepository.save(deliveryRoute);

        return deliveryRoute;
    }

    @Override
    @Transactional
    public DeliveryRouteResponseDto departDeliveryRoute(DeliveryRouteDepartRequestDto requestDto) {
        DeliveryRoute deliveryRoute = findDeliveryRoute(requestDto.getDeliveryId(),requestDto.getDepartureHubId(),requestDto.getArrivalHubId(), DeliveryRouteStatus.HUB_PENDING);

        //todo: 배송 매니저 유효성 check
        if(deliveryRoute.getSequence() > 1 && !deliveryRouteRepository.isPreviousSequenceDeparted(requestDto.getDeliveryId(),deliveryRoute.getSequence())){
            throw new BadRequestException("이전 배송이 도착하지 않아 배송을 시작할 수 없습니다.");
        }

        deliveryService.changeStatus(requestDto.getDeliveryId(), DeliveryStatus.HUB_TRANSIT);

        deliveryRoute.updateDepartDelivery(requestDto.getDeliveryManagerId());

        return DeliveryRouteResponseDto.fromEntity(deliveryRoute);
    }

    @Override
    @Transactional
    public DeliveryRouteResponseDto arriveDeliveryRoute(DeliveryRouteArriveRequestDto requestDto) {
        DeliveryRoute deliveryRoute = findDeliveryRoute(requestDto.getDeliveryId(),requestDto.getDepartureHubId(),requestDto.getArrivalHubId(), DeliveryRouteStatus.HUB_TRANSIT);

        deliveryRoute.updateArrivalDelivery(requestDto.getDistance(),requestDto.getDuration());

        deliveryService.changeStatus(requestDto.getDeliveryId(), DeliveryStatus.HUB_ARRIVED);

        return DeliveryRouteResponseDto.fromEntity(deliveryRoute);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryRouteResponseDto> getDeliveryRouteList(UUID deliveryId ,Pageable pageable) {
        return deliveryRouteRepository.findAllByDelivery_Id(deliveryId,pageable).map(DeliveryRouteResponseDto::fromEntity);
    }


    public DeliveryRoute findDeliveryRoute(UUID deliveryId, UUID departureHubId, UUID arrivalHubId, DeliveryRouteStatus status) {
        return deliveryRouteRepository.findByDelivery_IdAndDepartureHubIdAndArrivalHubIdAndStatus(
            deliveryId, departureHubId, arrivalHubId, status)
            .orElseThrow(()-> new NotFoundException("배송 경로를 찾을 수 없습니다."));
    }
}
