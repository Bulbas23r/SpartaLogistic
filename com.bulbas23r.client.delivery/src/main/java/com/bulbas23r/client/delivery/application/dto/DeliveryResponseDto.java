package com.bulbas23r.client.delivery.application.dto;

import com.bulbas23r.client.delivery.domain.model.Delivery;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryResponseDto {

    private UUID id;

    private UUID orderId;

    private UUID startHubId;

    private String startHubName;

    private UUID endHubId;

    private String endHubName;

    private String status;

    private String statusDescription;

    private UUID deliveryManagerId;

    private String deliveryManagerName;

    private UUID receiverCompanyId;

    private String receiverName;

    private String receiverCompanySlackId;

    public static DeliveryResponseDto fromEntity(Delivery delivery) {
        return DeliveryResponseDto.builder()
            .id(delivery.getId())
            .orderId(delivery.getOrderId())
            .startHubId(delivery.getStartHubId())
            .endHubId(delivery.getEndHubId())
            .status(delivery.getStatus().name())
            .statusDescription(delivery.getStatus().getDescription())
            .deliveryManagerId(delivery.getDeliveryManagerId())
            .receiverCompanyId(delivery.getReceiverCompanyId())
            .receiverCompanySlackId(delivery.getReceiverCompanySlackId())
            .build();
    }

    public DeliveryResponseDto(UUID id,UUID orderId,UUID startHubId, UUID endHubId, String status,
        UUID deliveryManagerId, UUID receiverCompanyId, String receiverCompanySlackId) {
        this.id = id;
        this.orderId = orderId;
        this.startHubId = startHubId;
        this.endHubId = endHubId;
        this.status = status;
        this.deliveryManagerId = deliveryManagerId;
        this.receiverCompanyId = receiverCompanyId;
        this.receiverCompanySlackId = receiverCompanySlackId;
    }
}
