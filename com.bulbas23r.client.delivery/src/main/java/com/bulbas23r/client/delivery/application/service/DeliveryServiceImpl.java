package com.bulbas23r.client.delivery.application.service;

import com.bulbas23r.client.delivery.application.dto.DeliveryRequestDto;
import com.bulbas23r.client.delivery.application.dto.DeliveryResponseDto;
import com.bulbas23r.client.delivery.domain.model.Delivery;
import com.bulbas23r.client.delivery.domain.repository.DeliveryRepository;
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

    @Override
    @Transactional
    public DeliveryResponseDto createDelivery(DeliveryRequestDto requestDto) {
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


    public Delivery findById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
            .orElseThrow(()-> new NotFoundException("배송 정보를 찾을 수 없습니다."));
    }
}
