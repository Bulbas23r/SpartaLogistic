package com.bulbas23r.client.product.infrastructure.messaging;

import common.event.CreateStockEventDto;
import common.event.DeleteProductEventDto;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProductEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendProductDeleteEvent(UUID productId){
        DeleteProductEventDto deleteProductEventDto = new DeleteProductEventDto(productId);
        kafkaTemplate.send("delete-order", deleteProductEventDto);
    }

    public void sendProductCreateEvent(UUID productId, UUID hubId, int quantity){
        CreateStockEventDto createStockEventDto = new CreateStockEventDto(
            hubId, productId, quantity
        );
        kafkaTemplate.send("create-stock", createStockEventDto);
    }
}
