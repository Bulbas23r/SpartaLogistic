package com.bulbas23r.client.order.presentation.dto;

import com.bulbas23r.client.order.domain.model.Order;
import com.bulbas23r.client.order.domain.model.OrderProduct;
import com.bulbas23r.client.order.domain.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderResponseDto {
    private UUID id;
    private UUID provideCompanyId;
    private UUID receiveCompanyId;
    private OrderStatus status;
    private Set<OrderProductResponseDto> orderProducts;

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.provideCompanyId = order.getProvideCompanyId();
        this.receiveCompanyId = order.getReceiveCompanyId();
        this.status = order.getStatus();
        this.orderProducts = order.getOrderProducts().stream()
            .map(OrderProductResponseDto::new)
            .collect(Collectors.toSet());
    }
}
