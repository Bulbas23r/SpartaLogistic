package com.bulbas23r.client.order.presentation.controller;

import com.bulbas23r.client.order.application.service.OrderService;
import com.bulbas23r.client.order.domain.model.Order;
import com.bulbas23r.client.order.infrastructure.string.OrderString;
import com.bulbas23r.client.order.presentation.dto.OrderCreateRequestDto;
import com.bulbas23r.client.order.presentation.dto.OrderResponseDto;
import com.bulbas23r.client.order.presentation.dto.OrderUpdateRequestDto;
import common.template.ApiResponse;
import common.utils.PageUtils;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
        @RequestBody OrderCreateRequestDto orderCreateRequestDto)
    {
        /**
         * TODO: 주문 시 주문 제품 중 허브에 물품 재고가 없는 경우 => 주문 실패로 처리됨
         * TODO: 있는 경우 => 주문 생성 => 주문 제품 수량 허브에서 감소 + 배송 생성
          */
        Order order = orderService.createOrder(orderCreateRequestDto);
        OrderResponseDto orderResponseDto = new OrderResponseDto(order);

        ApiResponse<OrderResponseDto> response = new ApiResponse<>(HttpStatus.CREATED.value(),
            OrderString.ORDER_CREATE, orderResponseDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrder(
        @PathVariable UUID orderId,
        @RequestBody OrderUpdateRequestDto orderUpdateRequestDto
    ) {
        Order order = orderService.updateOrder(orderId, orderUpdateRequestDto);
        OrderResponseDto orderResponseDto = new OrderResponseDto(order);
        ApiResponse<OrderResponseDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            OrderString.ORDER_UPDATE, orderResponseDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> deleteOrder(
        @PathVariable UUID orderId
    ){
        orderService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrder(
        @PathVariable UUID orderId
    ){
        Order order = orderService.getOrder(orderId);
        OrderResponseDto orderResponseDto = new OrderResponseDto(order);
        ApiResponse<OrderResponseDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            OrderString.ORDER_GET_BY_ID, orderResponseDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> getOrderList(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size
    ){
        Pageable pageable = PageUtils.pageable(page, size);
        Page<OrderResponseDto> orders = orderService.getAllOrders(pageable).map(OrderResponseDto::new);
        ApiResponse<Page<OrderResponseDto>> response = new ApiResponse<>(
            HttpStatus.OK.value(), OrderString.ORDER_GET, orders
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/orders/search")
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> searchOrders(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "DESC", required = false) Sort.Direction sortDirection,
        @RequestParam(defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<OrderResponseDto> orders = orderService.searchOrders(keyword, pageable, sortDirection,
            sortBy).map(OrderResponseDto::new);
        ApiResponse<Page<OrderResponseDto>> response = new ApiResponse<>(
            HttpStatus.OK.value(), OrderString.ORDER_GET, orders
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/orders/cancel/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(
        @PathVariable UUID orderId
    ){
        // TODO: 주문 취소 경우 => 주문 제품 수량만큼 허브에 복원
        orderService.cancelOrder(orderId);

        ApiResponse<OrderResponseDto> response = new ApiResponse<>(
            HttpStatus.OK.value(), OrderString.ORDER_CANCEL, null
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
