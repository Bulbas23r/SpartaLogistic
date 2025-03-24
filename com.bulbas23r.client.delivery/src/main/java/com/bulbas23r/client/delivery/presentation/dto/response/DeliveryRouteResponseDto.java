package com.bulbas23r.client.delivery.presentation.dto.response;

import com.bulbas23r.client.delivery.domain.model.DeliveryRoute;
import com.bulbas23r.client.delivery.domain.model.DeliveryRouteStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryRouteResponseDto {

    private UUID id;

    private UUID departureHubId;

    private UUID arrivalHubId;

    private UUID deliveryManagerId;

    private DeliveryRouteStatus status;

    private Integer sequence;

    private Integer estimatedDistance;

    private Integer estimatedDuration;

    private Integer actualDistance;

    private Integer actualDuration;


    public static DeliveryRouteResponseDto fromEntity(DeliveryRoute entity ) {
        return DeliveryRouteResponseDto.builder()
            .id(entity.getId())
            .departureHubId(entity.getDepartureHubId())
            .arrivalHubId(entity.getArrivalHubId())
            .status(entity.getStatus())
            .sequence(entity.getSequence())
            .estimatedDistance(entity.getEstimatedDistance())
            .estimatedDuration(entity.getEstimatedDuration())
            .actualDistance(entity.getActualDistance())
            .actualDuration(entity.getActualDuration())
            .build();
    }
}
