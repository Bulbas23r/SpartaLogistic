package com.bulbas23r.client.message.presentation.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryResponseDto {
  private UUID id;

  private UUID departureHubId;

  private UUID arrivalHubId;

  private UUID deliveryManagerId;

  private Integer sequence;

  private Integer estimatedDistance;

  private Integer estimatedDuration;

  private Integer actualDistance;

  private Integer actualDuration;
}
