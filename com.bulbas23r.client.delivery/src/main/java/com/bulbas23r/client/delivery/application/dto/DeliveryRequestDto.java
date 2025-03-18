package com.bulbas23r.client.delivery.application.dto;

import com.bulbas23r.client.delivery.domain.model.Delivery;
import com.bulbas23r.client.delivery.domain.model.DeliveryStatus;
import common.annotation.ValidUUID;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequestDto {

    @NotNull( message = "주문 Id는 필수 값 입니다.")
    private UUID orderId;

    @NotNull( message = "출발 허브 Id는 필수 값 입니다.")
    private UUID startHubId;

    @NotNull( message = "도착 허브 Id는 필수 값 입니다.")
    private UUID endHubId;

    @NotNull(message = "배송 매니저 Id는 필수 값 입니다.")
    private UUID deliveryManagerId;

    @Builder.Default
    private DeliveryStatus status = DeliveryStatus.HUB_PENDING;

    @NotNull( message = "수령 업체 Id는 필수 값 입니다.")
    private UUID receiverCompanyId;

    @NotNull( message = "수령 업체 slack Id는 필수 값 입니다.")
    private UUID receiverCompanySlackId;

    public Delivery toDelivery() {
        System.out.println("Converting deliveryManagerId: " + deliveryManagerId);
        return Delivery.builder()
            .orderId(this.orderId)
            .startHubId(this.startHubId)
            .endHubId(this.endHubId)
            .deliveryManagerId(this.deliveryManagerId)
            .status(this.status)
            .receiverCompanyId(this.receiverCompanyId)
            .receiverCompanySlackId(this.receiverCompanySlackId)
            .build();
    }

}
