package com.bulbas23r.client.order.domain.repository;

import com.bulbas23r.client.order.domain.model.Order;
import com.bulbas23r.client.order.domain.model.OrderStatus;
import common.utils.PageUtils.CommonSortBy;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface OrderQueryRepository {
    Page<Order> searchOrders(String keyword, Pageable pageable, Direction sortDirection, CommonSortBy sortBy);
    void updateOrderStatus(UUID orderId, OrderStatus orderStatus);
    List<Order> findOrdersByProductId(UUID productId);
}
