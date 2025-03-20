package com.bulbas23r.client.delivery.application.service;

import com.bulbas23r.client.delivery.application.dto.DeliveryCreateRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryResponseDto;
import com.bulbas23r.client.delivery.application.dto.DeliverySearchRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryUpdateRequestDto;
import com.bulbas23r.client.delivery.domain.model.Delivery;
import com.bulbas23r.client.delivery.domain.model.DeliveryStatus;
import com.bulbas23r.client.delivery.domain.repository.DeliveryQueryRepository;
import com.bulbas23r.client.delivery.domain.repository.DeliveryRepository;
import common.exception.BadRequestException;
import common.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryQueryRepository deliveryQueryRepository;

    @Override
    @Transactional
    public DeliveryResponseDto createDelivery(DeliveryCreateRequestDto requestDto) {
        //todo: _id 값 유효성 check

        Delivery delivery = requestDto.toDelivery();
        deliveryRepository.save(delivery);

        //todo: 배송 경로 생성

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

        if(!delivery.getStatus().equals(DeliveryStatus.READY)) {
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

        if(!delivery.getStatus().equals(DeliveryStatus.READY)) {
            throw new BadRequestException("배송 전에만 삭제 할 수 있습니다.");
        }

        delivery.setDeleted(true);

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


    public Delivery findById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
            .orElseThrow(()-> new NotFoundException("배송 정보를 찾을 수 없습니다."));
    }
}
