package com.bulbas23r.client.order.infrastructure.messaging;

import com.bulbas23r.client.order.domain.model.Order;
import com.bulbas23r.client.order.domain.model.OrderProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.event.CreateOrderEventDto;
import common.event.OrderProductEventDto;
import common.event.UpdateStockEventDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, Object> kafkaTemplate,
        ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderEvent(Order order) {
        Set<OrderProduct> orderProducts = order.getOrderProducts();
        UUID hubId = null;
        List<OrderProductEventDto> orderProductEventDtos = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            hubId = orderProduct.getHubId();
            OrderProductEventDto orderProductDto = new OrderProductEventDto(orderProduct.getProductId(),
                orderProduct.getQuantity());
            orderProductEventDtos.add(orderProductDto);
        }
        UpdateStockEventDto updateStockEventDto = new UpdateStockEventDto(hubId,
            orderProductEventDtos);
        kafkaTemplate.send("order-test-update-events", updateStockEventDto);
    }


    public void sendCreateOrderEventToDelivery(Order order) {
        CreateOrderEventDto orderEventDto = new CreateOrderEventDto(order.getId(),order.getProvideCompanyId(),order.getReceiveCompanyId());
        kafkaTemplate.send("create-order-delivery", orderEventDto);
    }
}
