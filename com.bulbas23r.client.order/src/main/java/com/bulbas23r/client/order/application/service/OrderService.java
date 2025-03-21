package com.bulbas23r.client.order.application.service;

import com.bulbas23r.client.order.domain.model.Order;
import com.bulbas23r.client.order.presentation.dto.OrderCreateRequestDto;
import com.bulbas23r.client.order.presentation.dto.OrderUpdateRequestDto;
import common.utils.PageUtils.CommonSortBy;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface OrderService {
    Order createOrder(OrderCreateRequestDto orderCreateRequestDto);
    void cancelOrder(UUID orderId);
    Order getOrder(UUID orderId);
    Order updateOrder(UUID orderId, OrderUpdateRequestDto orderUpdateRequestDto);
    void deleteOrder(UUID orderId);
    Page<Order> getAllOrders(Pageable pageable);
    Page<Order> searchOrders(String keywords, Pageable pageable, Direction direction, CommonSortBy sortBy);
}
