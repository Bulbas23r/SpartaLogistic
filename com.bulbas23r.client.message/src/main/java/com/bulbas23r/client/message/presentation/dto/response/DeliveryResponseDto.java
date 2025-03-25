package com.bulbas23r.client.message.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryResponseDto {
  @JsonProperty("id")
  private UUID id;

  @JsonProperty("departureHubId")
  private UUID departureHubId;

  @JsonProperty("arrivalHubId")
  private UUID arrivalHubId;

  @JsonProperty("deliveryManagerId")
  private UUID deliveryManagerId;

  @JsonProperty("sequence")
  private Integer sequence;

  @JsonProperty("estimatedDistance")
  private Integer estimatedDistance;

  @JsonProperty("estimatedDuration")
  private Integer estimatedDuration;

  @JsonProperty("actualDistance")
  private Integer actualDistance;

  @JsonProperty("actualDuration")
  private Integer actualDuration;

}
