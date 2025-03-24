package com.bulbas23r.client.message.presentation.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ProductResponseDto {
  private UUID id;
  private UUID companyId;
  private UUID hubId;
  private String name;
  private BigDecimal price;
  private String description;
}
