package com.bulbas23r.client.order.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class OrderUpdateRequestDto {
    @NotNull(message = "Provide Company ID is required")
    private UUID provideCompanyId;
    @NotNull(message = "Receive Company ID is required")
    private UUID receiveCompanyId;
    @NotNull(message = "Receive Company ID is required")
    private String status; // 주문 상태 (예: "RECEIVED", "CANCELLED", "COMPLETED")
    private List<OrderProductCreateRequestDto> orderProducts;
}
