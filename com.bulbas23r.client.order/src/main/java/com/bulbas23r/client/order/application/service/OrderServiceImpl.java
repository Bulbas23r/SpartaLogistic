package com.bulbas23r.client.order.application.service;

import com.bulbas23r.client.order.domain.model.Order;
import com.bulbas23r.client.order.domain.model.OrderStatus;
import com.bulbas23r.client.order.domain.repository.OrderQueryRepository;
import com.bulbas23r.client.order.domain.repository.OrderRepository;
import com.bulbas23r.client.order.presentation.dto.OrderCreateRequestDto;
import com.bulbas23r.client.order.presentation.dto.OrderUpdateRequestDto;
import common.utils.PageUtils.CommonSortBy;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @Transactional
    @Override
    public Order createOrder(OrderCreateRequestDto orderCreateRequestDto) {
        // TODO: order의 재고확인 로직 필요, 없으면 주문 생산 취소가 됨
        Order order = new Order(orderCreateRequestDto);
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public void cancelOrder(UUID orderId) {
        orderQueryRepository.updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    @Transactional
    @Override
    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    @Override
    public Order updateOrder(UUID orderId, OrderUpdateRequestDto orderUpdateRequestDto) {
        Order order = getOrder(orderId);
        order.update(orderUpdateRequestDto);
        orderRepository.update(order);
        return order;
    }

    @Transactional
    @Override
    public void deleteOrder(UUID orderId) {
        Order order = getOrder(orderId);
        order.setDeleted(true);
    }

    @Transactional
    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAllByIsDeletedIsFalse(pageable);
    }

    @Override
    public Page<Order> searchOrders(String keywords, Pageable pageable, Direction direction,
        CommonSortBy sortBy) {
        return orderQueryRepository.searchOrders(keywords, pageable, direction, sortBy);
    }
}
