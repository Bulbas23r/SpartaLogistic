package com.bulbas23r.client.message.presentation.dto;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@RequiredArgsConstructor
public class OrderReseponseDto {
  private UUID id;
  private UUID provideCompanyId;
  private UUID receiveCompanyId;
  private Set<OrderProductResponseDto> orderProducts;

  @Getter
  @Setter
  @RequiredArgsConstructor
  public static class OrderProductResponseDto {
    private UUID id;
    private UUID hubId;
    private UUID productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private String memo;
  }
}
