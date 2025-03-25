package com.bulbas23r.client.delivery.application.service;

import com.bulbas23r.client.delivery.domain.model.DeliveryRoute;
import com.bulbas23r.client.delivery.domain.model.DeliveryRouteStatus;
import com.bulbas23r.client.delivery.domain.model.DeliveryStatus;
import com.bulbas23r.client.delivery.domain.repository.DeliveryRouteRepository;
import com.bulbas23r.client.delivery.infrastructure.client.DeliverManageClient;
import com.bulbas23r.client.delivery.presentation.dto.request.DeliveryRouteArriveRequestDto;
import com.bulbas23r.client.delivery.presentation.dto.request.DeliveryRouteDepartRequestDto;
import com.bulbas23r.client.delivery.presentation.dto.response.DeliveryManagerResponseDto;
import com.bulbas23r.client.delivery.presentation.dto.response.DeliveryRouteResponseDto;
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

    private final DeliverManageClient deliverManageClient;

    @Override
    @Transactional
    public DeliveryRouteResponseDto departDeliveryRoute(DeliveryRouteDepartRequestDto requestDto) {
        DeliveryManagerResponseDto managerInfo = deliverManageClient.getDeliveryManagerByUserId(requestDto.getDeliveryManagerId());
        if(managerInfo == null || !managerInfo.getDeliveryManagerType().equals("HUB")) {
            throw new BadRequestException("배송 담당자가 유효하지 않거나 허브 배송 담당자가 아닙니다.");
        }

        DeliveryRoute deliveryRoute = findDeliveryRoute(requestDto.getDeliveryId(),
            requestDto.getDepartureHubId(), requestDto.getArrivalHubId(),
            DeliveryRouteStatus.HUB_PENDING);

        if (deliveryRoute.getSequence() > 1 && !deliveryRouteRepository.isPreviousSequenceDeparted(
            requestDto.getDeliveryId(), deliveryRoute.getSequence())) {
            throw new BadRequestException("이전 배송이 도착하지 않아 배송을 시작할 수 없습니다.");
        }

        deliveryService.changeStatus(requestDto.getDeliveryId(), DeliveryStatus.HUB_TRANSIT);

        deliveryRoute.updateDepartDelivery(requestDto.getDeliveryManagerId());

        return DeliveryRouteResponseDto.fromEntity(deliveryRoute);
    }

    @Override
    @Transactional
    public DeliveryRouteResponseDto arriveDeliveryRoute(DeliveryRouteArriveRequestDto requestDto) {
        DeliveryRoute deliveryRoute = findDeliveryRoute(requestDto.getDeliveryId(),
            requestDto.getDepartureHubId(), requestDto.getArrivalHubId(),
            DeliveryRouteStatus.HUB_TRANSIT);

        deliveryRoute.updateArrivalDelivery(requestDto.getDistance(), requestDto.getDuration());

        deliveryService.changeStatus(requestDto.getDeliveryId(), DeliveryStatus.HUB_ARRIVED);

        return DeliveryRouteResponseDto.fromEntity(deliveryRoute);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryRouteResponseDto> getDeliveryRouteList(UUID deliveryId, Pageable pageable) {
        return deliveryRouteRepository.findAllByDelivery_Id(deliveryId, pageable)
            .map(DeliveryRouteResponseDto::fromEntity);
    }

    @Override
    public Page<DeliveryRouteResponseDto> getDeliveryByOrderIdRouteList(UUID orderId,
        Pageable pageable) {
        return deliveryRouteRepository.findAllByDelivery_OrderId(orderId, pageable)
            .map(DeliveryRouteResponseDto::fromEntity);
    }

    public DeliveryRoute findDeliveryRoute(UUID deliveryId, UUID departureHubId, UUID arrivalHubId,
        DeliveryRouteStatus status) {
        return deliveryRouteRepository.findByDelivery_IdAndDepartureHubIdAndArrivalHubIdAndStatus(
                deliveryId, departureHubId, arrivalHubId, status)
            .orElseThrow(() -> new NotFoundException("배송 경로를 찾을 수 없습니다."));
    }
}
