package com.bulbas23r.client.product.infrastructure.messaging;

import common.event.CreateProductEventDto;
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

    public void sendProductDeleteEvent(UUID productId, UUID hubId){
        DeleteProductEventDto deleteProductEventDto = new DeleteProductEventDto(
            hubId, productId);
        kafkaTemplate.send("delete-product", deleteProductEventDto);
    }

    public void sendProductCreateEvent(UUID productId, UUID hubId, int quantity){
        CreateProductEventDto createProductEventDto = new CreateProductEventDto(
            hubId, productId, quantity
        );
        kafkaTemplate.send("create-product", createProductEventDto);
    }
}
