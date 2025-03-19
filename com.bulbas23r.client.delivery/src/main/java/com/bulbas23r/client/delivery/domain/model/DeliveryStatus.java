package com.bulbas23r.client.delivery.domain.model;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    READY("배송 준비"),
    HUB_PENDING("허브 대기"),
    HUB_TRANSIT("허브 간 이동"),
    HUB_ARRIVED("허브 도착"),
    COMPANY_TRANSIT("업체 배송 중"),
    DELIVERED("배송 완료");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

}
