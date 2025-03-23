package com.bulbas23r.client.product.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequestDto {
    @NotNull(message = "Company ID is required")
    private UUID companyId;

    @NotNull(message = "Hub ID is required")
    private UUID hubId;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Product quantity is required")
    private int quantity;

    // 설명은 선택적으로 입력 가능
    private String description;

    // 모든 필드를 받는 생성자
    public ProductCreateRequestDto(UUID companyId, UUID hubId, String name,
        BigDecimal price, int quantity, String description) {
        this.companyId = companyId;
        this.hubId = hubId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }
}
