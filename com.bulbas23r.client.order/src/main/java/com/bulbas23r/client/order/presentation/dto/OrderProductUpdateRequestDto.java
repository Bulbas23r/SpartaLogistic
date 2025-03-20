package com.bulbas23r.client.order.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class OrderProductUpdateRequestDto {
    @NotNull(message = "Hub ID is required")
    private UUID hubId;
    @NotNull(message = "Product ID is required")
    private UUID productId;
    @NotNull(message = "Product name ID is required")
    private String productName;
    @NotNull(message = "Quantity ID is required")
    private int quantity;
    @NotNull(message = "Price ID is required")
    private BigDecimal price;

    private String memo;
}
