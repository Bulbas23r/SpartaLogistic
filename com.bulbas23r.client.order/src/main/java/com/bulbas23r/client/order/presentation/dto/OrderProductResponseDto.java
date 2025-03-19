package com.bulbas23r.client.order.presentation.dto;

import com.bulbas23r.client.order.domain.model.OrderProduct;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderProductResponseDto {
    private UUID id;
    private UUID hubId;
    private UUID productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private String memo;

    public OrderProductResponseDto(OrderProduct orderProduct) {
        this.id = orderProduct.getId();
        this.hubId = orderProduct.getHubId();
        this.productId = orderProduct.getProductId();
        this.productName = orderProduct.getProductName();
        this.quantity = orderProduct.getQuantity();
        this.price = orderProduct.getPrice();
        this.memo = orderProduct.getMemo();
    }
}
