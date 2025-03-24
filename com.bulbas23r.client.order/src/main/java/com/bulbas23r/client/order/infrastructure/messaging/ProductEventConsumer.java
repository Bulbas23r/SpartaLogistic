package com.bulbas23r.client.order.infrastructure.messaging;

import com.bulbas23r.client.order.domain.model.Order;
import com.bulbas23r.client.order.domain.repository.OrderQueryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.event.DeleteProductEventDto;
import common.event.GroupId;
import common.event.Topic;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductEventConsumer {

    private static Logger logger = LoggerFactory.getLogger(ProductEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final OrderQueryRepository orderQueryRepository;

    public ProductEventConsumer(ObjectMapper objectMapper,
        OrderQueryRepository orderQueryRepository) {
        this.objectMapper = objectMapper;
        this.orderQueryRepository = orderQueryRepository;
    }

    @KafkaListener(topics = Topic.DELETE_PRODUCT, groupId = GroupId.ORDER)
    public void handleDeleteOrderEvent(Map<String, Object> event) {
        DeleteProductEventDto deleteProductEventDto = objectMapper.convertValue(event,
            DeleteProductEventDto.class);
        UUID productId = deleteProductEventDto.getProductId();

        List<Order> ordersByProductId = orderQueryRepository.findOrdersByProductId(productId);
        if (ordersByProductId != null && !ordersByProductId.isEmpty()) {
            ordersByProductId.forEach(order -> {
                order.setDeleted(true);
            });
            logger.info("Deleted {} Orders for productId {}", ordersByProductId, productId);
        } else {
            logger.info("No Orders found for productId {}", productId);
        }
    }
}
