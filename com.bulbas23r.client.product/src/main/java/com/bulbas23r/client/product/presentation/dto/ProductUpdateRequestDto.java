package com.bulbas23r.client.product.presentation.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductUpdateRequestDto {
    private UUID companyId;
    private UUID hubId;
    private String name;
    private BigDecimal price;
    private String description;
}
