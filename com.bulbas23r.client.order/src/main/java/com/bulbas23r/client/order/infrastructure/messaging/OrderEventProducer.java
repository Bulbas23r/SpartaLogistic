package com.bulbas23r.client.order.infrastructure.messaging;

import com.bulbas23r.client.order.domain.model.Order;
import com.bulbas23r.client.order.domain.model.OrderProduct;
import common.event.CancelOrderEventDto;
import common.event.CreateOrderEventDto;
import common.event.OrderProductEventDto;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Order craete Event 발행 -> "create-order" 토픽 리스너만 청취 가능
     * @param order
     */
    public void sendOrderCreateEvent(Order order) {
        UUID hubId = getHubId(order);
        List<OrderProductEventDto> createOrderProductEventDtos = getOrderProductEventDtos(order, 1);

        CreateOrderEventDto createOrderEventDto = new CreateOrderEventDto(order.getId(), hubId, createOrderProductEventDtos);
        kafkaTemplate.send("create-order", createOrderEventDto);
    }

    /**
     * Order cancel Event 발행 -> "cancel-order" 토픽 리스너만 청취 가능
     * @param order
     */
    public void sendOrderCancelEvent(Order order) {
        UUID hubId = getHubId(order);
        List<OrderProductEventDto> cancelOrderProductEventDtos = getOrderProductEventDtos(order, -1);

        CancelOrderEventDto cancelOrderEventDto = new CancelOrderEventDto(order.getId(), hubId, cancelOrderProductEventDtos);
        kafkaTemplate.send("cancel-order", cancelOrderEventDto);
    }


    private List<OrderProductEventDto> getOrderProductEventDtos(Order order, int quantityOperator) {

        return order.getOrderProducts().stream()
            .map(orderProduct -> new OrderProductEventDto(orderProduct.getId(),
                quantityOperator * orderProduct.getQuantity()))
            .collect(Collectors.toList());
    }

    /**
     * Order에서 HubID뽑기 (Order 내 모든 제품은 동일한 hubId)
     * @param order
     * @return
     */
    private UUID getHubId(Order order) {
        return order.getOrderProducts().stream()
            .findFirst()
            .map(OrderProduct::getHubId)
            .orElse(null);
    }
}
