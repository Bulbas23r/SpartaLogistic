package com.bulbas23r.client.order.infrastructure.messaging;

import com.bulbas23r.client.order.domain.model.Order;
import com.bulbas23r.client.order.domain.model.OrderProduct;
import common.event.CancelOrderEventDto;
import common.event.CancelOrderProductEventDto;
import common.event.CreateOrderEventDto;
import common.event.CreateOrderProductEventDto;
import common.event.UpdateStockEventDto;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
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
        List<CreateOrderProductEventDto> createOrderProductEventDtos = convertOrderProductEventDtos(
            order, 1,
            op -> new CreateOrderProductEventDto(op.getProductId(), op.getQuantity()));

        CreateOrderEventDto createOrderEventDto = new CreateOrderEventDto(order.getId(), hubId, createOrderProductEventDtos);
        kafkaTemplate.send("create-order", createOrderEventDto);
    }

    /**
     * Order cancel Event 발행 -> "cancel-order" 토픽 리스너만 청취 가능
     * @param order
     */
    public void sendOrderCancelEvent(Order order) {
        UUID hubId = getHubId(order);
        List<CancelOrderProductEventDto> cancelOrderProductEventDtos = convertOrderProductEventDtos(
            order, 1,
            op -> new CancelOrderProductEventDto(op.getProductId(), op.getQuantity()));

        CancelOrderEventDto cancelOrderEventDto = new CancelOrderEventDto(order.getId(), hubId, cancelOrderProductEventDtos);
        kafkaTemplate.send("cancel-order", cancelOrderEventDto);
    }

    /**
     * OrderProduct를 특정 OrderProductEvent로 변환
     * @param order
     * @param quantityMultiplier
     * @param mapper
     * @return
     * @param <T>
     */
    private <T> List<T> convertOrderProductEventDtos(Order order, int quantityMultiplier ,
        Function<OrderProduct, T> mapper) {

        return order.getOrderProducts().stream()
            .map(mapper)
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
