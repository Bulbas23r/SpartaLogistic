package com.bulbas23r.client.delivery.domain.repository;

import com.bulbas23r.client.delivery.application.dto.DeliveryResponseDto;
import common.utils.PageUtils.CommonSortBy;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface DeliveryQueryRepository {
    Page<DeliveryResponseDto> searchDelivery(UUID orderId,UUID startHubId, UUID endHubId,
        UUID deliveryManagerId, UUID receiverCompanyId,
        Pageable pageable, Direction sortDirection, CommonSortBy sortBy );

}
