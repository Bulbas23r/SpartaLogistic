package com.bulbas23r.client.order.infrastructure.messaging;

import com.bulbas23r.client.order.domain.model.Order;
import com.bulbas23r.client.order.domain.model.OrderProduct;
import common.event.UpdateOrderProductEventDto;
import common.event.UpdateStockEventDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
     * Order Create/update to hub-stock service in hub domain
     * @param order
     * @param quantityMultiplier
     */
    public void sendOrderEvent(Order order, int quantityMultiplier) {
        // 모든 주문 제품은 동일한 hubId를 소유
        UUID hubId = order.getOrderProducts().stream()
            .findFirst()
            .map(OrderProduct::getHubId)
            .orElse(null);

        List<UpdateOrderProductEventDto> eventDtos = order.getOrderProducts().stream()
            .map(orderProduct -> new UpdateOrderProductEventDto(
                orderProduct.getProductId(),
                orderProduct.getQuantity() * quantityMultiplier))
            .collect(Collectors.toList());

        UpdateStockEventDto updateStockEventDto = new UpdateStockEventDto(hubId, eventDtos);
        kafkaTemplate.send("update-stock", updateStockEventDto);
    }
}
