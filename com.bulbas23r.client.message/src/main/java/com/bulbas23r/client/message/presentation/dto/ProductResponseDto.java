package com.bulbas23r.client.message.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ProductResponseDto {
  @JsonProperty("id")
  private UUID id;
  @JsonProperty("companyId")
  private UUID companyId;
  @JsonProperty("hubId")
  private UUID hubId;
  @JsonProperty("name")
  private String name;
  @JsonProperty("price")
  private BigDecimal price;
  @JsonProperty("description")
  private String description;
}
