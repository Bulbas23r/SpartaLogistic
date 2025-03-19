package com.bulbas23r.client.deliverymanager.application.service;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManagerType;
import com.bulbas23r.client.deliverymanager.domain.repository.DeliveryManagerRepository;
import com.bulbas23r.client.deliverymanager.presentation.dto.CreateDeliveryManagerRequestDto;
import common.exception.BadRequestException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryManagerServiceImpl implements DeliveryManagerService {

    private final DeliveryManagerRepository deliveryManagerRepository;

    @Override
    public DeliveryManager createDeliveryManager(CreateDeliveryManagerRequestDto requestDto) {
        // TODO userID, hubID 검증 로직 추가하기

        Integer sequence;
        if (requestDto.getDeliveryManagerType().equals(DeliveryManagerType.HUB)) {
            sequence = getHubDeliveryManagerSequence();
        } else {
            if (requestDto.getHubId() == null) {
                throw new BadRequestException("업체 배송 담당자를 등록하려면 Hub id가 필요합니다!");
            }
            sequence = getCompanyDeliveryManagerSequence(requestDto.getHubId());
        }

        DeliveryManager deliveryManager = new DeliveryManager(requestDto, sequence);

        return deliveryManagerRepository.save(deliveryManager);
    }

    private Integer getHubDeliveryManagerSequence() {
        List<DeliveryManager> deliveryManagers = deliveryManagerRepository.findAllByTypeOrderBySequenceAsc(
            DeliveryManagerType.HUB);

        if (deliveryManagers.size() == 10) {
            throw new BadRequestException("허브 배송 담당자의 정원이 모두 찼습니다!");
        }

        return calculateSequence(deliveryManagers);
    }

    private Integer getCompanyDeliveryManagerSequence(UUID hubId) {
        List<DeliveryManager> deliveryManagers =
            deliveryManagerRepository.findAllByTypeAndHubIdOrderBySequenceAsc(
                DeliveryManagerType.COMPANY, hubId);

        if (deliveryManagers.size() == 10) {
            throw new BadRequestException("업체 배송 담당자의 정원이 모두 찼습니다!");
        }

        return calculateSequence(deliveryManagers);
    }

    private Integer calculateSequence(List<DeliveryManager> deliveryManagers) {
        Integer sequence = 1;
        for (DeliveryManager deliveryManager : deliveryManagers) {
            if (!sequence.equals(deliveryManager.getSequence())) {
                return sequence;
            }
            sequence++;
        }

        return sequence;
    }
}
