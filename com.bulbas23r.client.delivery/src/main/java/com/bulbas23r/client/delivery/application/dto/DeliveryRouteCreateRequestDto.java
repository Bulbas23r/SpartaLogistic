package com.bulbas23r.client.delivery.application.dto;

import com.bulbas23r.client.delivery.domain.model.Delivery;
import com.bulbas23r.client.delivery.domain.model.DeliveryRoute;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeliveryRouteCreateRequestDto {

    private UUID deliveryId;

    private UUID departureHubId;

    private Integer sequence;

    private UUID arrivalHubId;

    private Integer transitTime;

    private Integer transitDistance;

    public DeliveryRoute toDeliveryRoute(Delivery delivery) {
        return DeliveryRoute.builder()
            .delivery(delivery)
            .departureHubId(departureHubId)
            .sequence(sequence)
            .arrivalHubId(arrivalHubId)
            .departureHubId(departureHubId)
            .estimatedDistance(transitDistance)
            .estimatedDuration(transitTime)
            .build();

    }

}
