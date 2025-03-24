package com.bulbas23r.client.delivery.presentation.dto.request;

import common.utils.PageUtils;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class DeliverySearchRequestDto {

    private UUID orderId;

    private UUID startHubId;

    private UUID endHubId;

    private UUID deliveryManagerId;

    private UUID receiverCompanyId;

    private int page = 0;
    private int size = 10;
    private Sort.Direction sortDirection = Sort.Direction.DESC;
    private PageUtils.CommonSortBy sortBy = PageUtils.CommonSortBy.UPDATED_AT;

    public Pageable toPageable() {
        return PageUtils.pageable(page, size);
    }
}
