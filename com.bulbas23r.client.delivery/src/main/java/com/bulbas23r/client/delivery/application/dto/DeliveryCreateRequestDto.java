package com.bulbas23r.client.delivery.application.dto;

import com.bulbas23r.client.delivery.domain.model.Delivery;
import com.bulbas23r.client.delivery.domain.model.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCreateRequestDto {

    @NotNull( message = "주문 Id는 필수 값 입니다.")
    private UUID orderId;

    @NotNull( message = "출발 허브 Id는 필수 값 입니다.")
    private UUID startHubId;

    @NotNull( message = "도착 허브 Id는 필수 값 입니다.")
    private UUID endHubId;

    @NotNull(message = "배송 매니저 Id는 필수 값 입니다.")
    private UUID deliveryManagerId;

    @Builder.Default
    private DeliveryStatus status = DeliveryStatus.READY;

    @NotNull( message = "수령 업체 Id는 필수 값 입니다.")
    private UUID receiverCompanyId;

    @NotNull( message = "수령 업체 slack Id는 필수 값 입니다.")
    private UUID receiverCompanySlackId;

    public Delivery toDelivery() {
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
