package com.bulbas23r.client.product.presentation.dto;

import com.bulbas23r.client.product.domain.model.Product;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductResponseDto {
    private UUID id;
    private UUID companyId;
    private UUID hubId;
    private String name;
    private BigDecimal price;
    private String description;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.companyId = product.getCompanyId();
        this.hubId = product.getHubId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
    }
}
