package com.bulbas23r.client.product.infrastructure.messaging;

import com.bulbas23r.client.product.domain.model.Product;
import com.bulbas23r.client.product.domain.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.event.DeleteHubEventDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class HubEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(HubEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    public HubEventConsumer(ObjectMapper objectMapper, ProductRepository productRepository) {
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "delete-hub")
    public void handleDeleteHubEvent(Map<String, Object> eventMap) {
        DeleteHubEventDto deleteStockEventDto = objectMapper.convertValue(eventMap,
            DeleteHubEventDto.class);
        UUID hubId = deleteStockEventDto.getHubId();

        List<Product> productsToDelete = productRepository.findByHubId(hubId);
        if(productsToDelete != null && !productsToDelete.isEmpty()) {
            productsToDelete.forEach(productRepository::delete);
            logger.info("Deleted {} products for hubId {}", productsToDelete.size(), hubId);
        }
        else{
            logger.info("No products found for hubId {}", hubId);
        }
    }
}
