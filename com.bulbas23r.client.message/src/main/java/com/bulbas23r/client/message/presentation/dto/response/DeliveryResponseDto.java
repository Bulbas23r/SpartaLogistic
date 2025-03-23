package com.bulbas23r.client.message.presentation.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryResponseDto {
  private UUID departureHubId;
  private UUID arrivalHubId;
  private Integer sequence;
}
