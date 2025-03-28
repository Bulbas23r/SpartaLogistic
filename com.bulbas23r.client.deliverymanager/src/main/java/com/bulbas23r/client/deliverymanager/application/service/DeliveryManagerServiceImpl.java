package com.bulbas23r.client.deliverymanager.application.service;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManager;
import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManagerType;
import com.bulbas23r.client.deliverymanager.domain.repository.DeliveryManagerQueryRepository;
import com.bulbas23r.client.deliverymanager.domain.repository.DeliveryManagerRepository;
import com.bulbas23r.client.deliverymanager.infrastructure.client.HubClient;
import com.bulbas23r.client.deliverymanager.infrastructure.client.UserClient;
import com.bulbas23r.client.deliverymanager.presentation.dto.CreateDeliveryManagerRequestDto;
import com.bulbas23r.client.deliverymanager.presentation.dto.UpdateDeliveryManagerRequestDto;
import common.dto.HubInfoResponseDto;
import common.dto.UserInfoResponseDto;
import common.exception.BadRequestException;
import common.exception.NotFoundException;
import common.model.UserRoleEnum;
import common.utils.PageUtils.CommonSortBy;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryManagerServiceImpl implements DeliveryManagerService {

    private final DeliveryManagerRepository deliveryManagerRepository;
    private final DeliveryManagerQueryRepository deliveryManagerQueryRepository;
    private final UserClient userClient;
    private final HubClient hubClient;

    @Transactional
    @Override
    public DeliveryManager createDeliveryManager(CreateDeliveryManagerRequestDto requestDto) {
        if (requestDto.getDeliveryManagerType() == DeliveryManagerType.COMPANY) {
            if (requestDto.getHubId() == null) {
                throw new BadRequestException("업체 배송 담당자가 속할 허브 id가 필요합니다!");
            }

            HubInfoResponseDto hubInfo = hubClient.getHubInfoById(requestDto.getHubId()).getBody();
            if (hubInfo == null || hubInfo.getActive() == null) {
                throw new BadRequestException("허브가 존재하지 않거나 유효하지 않습니다!");
            }
        }

        UserInfoResponseDto userInfo = userClient.getUserInfo(requestDto.getUserId()).getBody();
        if (userInfo == null || !(userInfo.getRole().equals(UserRoleEnum.HUB_TO_HUB_DELIVERY)
            || userInfo.getRole().equals(UserRoleEnum.TO_COMPANY_DELIVERY))) {
            throw new BadRequestException("유저가 존재하지 않거나 배송담당자가 아닙니다.");
        }

        String slackId = userInfo.getSlackId();
        Integer seq = getSequence(requestDto);

        DeliveryManager deliveryManager = new DeliveryManager(requestDto, seq + 1, slackId);

        return deliveryManagerRepository.save(deliveryManager);
    }

    @Transactional
    @Override
    public void deleteDeliveryManager(Long userId) {
        DeliveryManager deliveryManager = getDeliveryManager(userId);

        deliveryManager.setDeleted();
    }

    @Transactional
    @Override
    public DeliveryManager updateDeliveryManager(Long userId,
        UpdateDeliveryManagerRequestDto requestDto) {
        DeliveryManager deliveryManager = getDeliveryManager(userId);

        if (!deliveryManager.getSequence().equals(requestDto.getSequence())) {
            if (deliveryManager.getType().equals(DeliveryManagerType.HUB)) {
                if (deliveryManagerRepository.existsByTypeAndSequence(DeliveryManagerType.HUB,
                    requestDto.getSequence())) {
                    throw new BadRequestException("이미 존재하는 순서입니다!");
                }
            } else {
                if (deliveryManagerRepository.existsByTypeAndHubIdAndSequence(
                    DeliveryManagerType.COMPANY, deliveryManager.getHubId(),
                    requestDto.getSequence())) {
                    throw new BadRequestException("이미 존재하는 순서입니다!");
                }
            }
            deliveryManager.setSequence(requestDto.getSequence());
        }

        return deliveryManager;
    }

    @Transactional(readOnly = true)
    @Override
    public DeliveryManager getDeliveryManager(Long userId) {
        return deliveryManagerRepository.findById(userId).orElseThrow(
            () -> new NotFoundException("존재하지 않는 담당자입니다!")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DeliveryManager> getDeliveryManagerList(Pageable pageable) {
        return deliveryManagerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DeliveryManager> getHubDeliveryManagerList() {
        return deliveryManagerRepository.findAllByTypeOrderBySequenceAsc(DeliveryManagerType.HUB);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DeliveryManager> getCompanyDeliveryManagerList(UUID hubId) {
        return deliveryManagerRepository.findAllByTypeAndHubIdOrderBySequenceAsc(
            DeliveryManagerType.COMPANY, hubId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DeliveryManager> searchDeliveryManagerList(Pageable pageable,
        Direction sortDirection, CommonSortBy sortBy, String keyword) {
        return deliveryManagerQueryRepository.searchDeliveryManager(
            pageable, sortDirection, sortBy, keyword);
    }

    @Override
    public boolean checkDeliveryManager(Long userId, DeliveryManager deliveryManager) {
        return userId.equals(deliveryManager.getUserId());
    }

    private Integer getSequence(CreateDeliveryManagerRequestDto requestDto) {
        if (requestDto.getDeliveryManagerType().equals(DeliveryManagerType.HUB)) {
            Integer count = deliveryManagerRepository.countByType(
                requestDto.getDeliveryManagerType());
            if (count >= 10) {
                throw new BadRequestException("허브 배송 담당자의 정원이 모두 찼습니다!");
            }
            return deliveryManagerRepository.findMaxSequenceByType(
                requestDto.getDeliveryManagerType());
        } else {
            if (requestDto.getHubId() == null) {
                throw new BadRequestException("허브 아이디가 필요합니다!");
            }
            Integer count = deliveryManagerRepository.countByTypeAndHubId(
                requestDto.getDeliveryManagerType(), requestDto.getHubId());
            if (count >= 10) {
                throw new BadRequestException("업체 배송 담당자의 정원이 모두 찼습니다!");
            }
            return deliveryManagerRepository.findMaxSequenceByTypeAndHubId(
                requestDto.getDeliveryManagerType(), requestDto.getHubId());
        }
    }

    @Override
    public void checkHubManager(Long requestUserId, UUID hubId) {
        if (hubId == null) {
            throw new BadRequestException("허브 관리자를 검증하려면 허브 아이디가 필요합니다!");
        }
        HubInfoResponseDto hubInfo = hubClient.getHubInfoById(hubId).getBody();
        UserInfoResponseDto userInfo = userClient.getUserInfo(requestUserId).getBody();

        if (userInfo == null || !userInfo.getRole().equals(UserRoleEnum.HUB_MANAGER)
            || hubInfo == null || hubInfo.getManagerId() == null || !hubInfo.getManagerId()
            .equals(userInfo.getUserId())) {
            throw new BadRequestException("본인의 관리하는 허브의 배송 담당자만 조회할 수 있습니다!");
        }

    }

    @Override
    @Transactional
    public void deleteCompanyDeliveryManagers(UUID hubId) {
        List<DeliveryManager> deliveryManagers = getCompanyDeliveryManagerList(hubId);
        deliveryManagers.forEach(DeliveryManager::setDeleted);
    }
    //    @Transactional
//    @Override
//    public DeliveryManager createDeliveryManager(CreateDeliveryManagerRequestDto requestDto) {
//        Integer sequence;
//        if (requestDto.getDeliveryManagerType().equals(DeliveryManagerType.HUB)) {
//            sequence = getHubDeliveryManagerSequence();
//        } else {
//            if (requestDto.getHubId() == null) {
//                throw new BadRequestException("업체 배송 담당자를 등록하려면 Hub id가 필요합니다!");
//            }
//            sequence = getCompanyDeliveryManagerSequence(requestDto.getHubId());
//        }
//
//        DeliveryManager deliveryManager = new DeliveryManager(requestDto, sequence);
//
//        return deliveryManagerRepository.save(deliveryManager);
//    }
//
//    @Transactional
//    @Override
//    public void deleteDeliveryManager(Long userId) {
//        DeliveryManager deliveryManager = getDeliveryManager(userId);
//
//        // 이후 seq 1씩 줄이기
//        List<DeliveryManager> deliveryManagers;
//        if (deliveryManager.getType().equals(DeliveryManagerType.HUB)) {
//            deliveryManagers = deliveryManagerRepository.findAllByTypeOrderBySequenceAsc(
//                DeliveryManagerType.HUB);
//        } else {
//            deliveryManagers = deliveryManagerRepository.findAllByTypeAndHubIdOrderBySequenceAsc(
//                DeliveryManagerType.COMPANY, deliveryManager.getHubId());
//        }
//        int index = deliveryManagers.indexOf(deliveryManager);
//        for (int i = index + 1; i < deliveryManagers.size(); i++) {
//            deliveryManagers.get(i).setSequence(i - 1);
//        }
//
//        deliveryManagerRepository.save
//        deliveryManager.setDeleted();
//    }
//
//    @Transactional
//    @Override
//    public DeliveryManager updateDeliveryManager(Long userId,
//        UpdateDeliveryManagerRequestDto requestDto) {
//        DeliveryManager deliveryManager = getDeliveryManager(userId);
//        deliveryManager.setSlackId(requestDto.getSlackId());
//
//        if (!deliveryManager.getSequence().equals(requestDto.getSequence())) {
//            List<DeliveryManager> deliveryManagers;
//            if (deliveryManager.getType().equals(DeliveryManagerType.HUB)) {
//                deliveryManagers = deliveryManagerRepository.findAllByTypeOrderBySequenceAsc(
//                    DeliveryManagerType.HUB);
//            } else {
//                deliveryManagers = deliveryManagerRepository.findAllByTypeAndHubIdOrderBySequenceAsc(
//                    DeliveryManagerType.COMPANY, deliveryManager.getHubId());
//            }
//            for (DeliveryManager manager : deliveryManagers) {
//                // 바꾸려는 seq가 이미 존재할 경우
//                if (manager.getSequence().equals(requestDto.getSequence())) {
//
//                }
//            }
//        }
//    }
//
//    private Integer getHubDeliveryManagerSequence() {
//        Integer max = deliveryManagerRepository.findMaxSequenceByType(
//            DeliveryManagerType.HUB);
//
//        if (max == 10) {
//            throw new BadRequestException("허브 배송 담당자의 정원이 모두 찼습니다!");
//        }
//
//        return max + 1;
//    }
//
//    private Integer getCompanyDeliveryManagerSequence(UUID hubId) {
//        Integer max = deliveryManagerRepository.findMaxSequenceByTypeAndHubId(
//            DeliveryManagerType.COMPANY, hubId);
//
//        if (max == 10) {
//            throw new BadRequestException("업체 배송 담당자의 정원이 모두 찼습니다!");
//        }
//
//        return max + 1;
//    }
//
//    private Integer calculateSequence(List<DeliveryManager> deliveryManagers) {
//        Integer sequence = 1;
//        for (DeliveryManager deliveryManager : deliveryManagers) {
//            if (!sequence.equals(deliveryManager.getSequence())) {
//                return sequence;
//            }
//            sequence++;
//        }
//
//        return sequence;
//    }
}
