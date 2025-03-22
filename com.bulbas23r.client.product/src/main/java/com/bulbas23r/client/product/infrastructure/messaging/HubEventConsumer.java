package com.bulbas23r.client.product.infrastructure.messaging;

import com.bulbas23r.client.product.domain.model.Product;
import com.bulbas23r.client.product.domain.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.event.DeleteStockEventDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class HubEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(HubEventConsumer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    public HubEventConsumer(KafkaTemplate<String, Object> kafkaTemplate,
        ObjectMapper objectMapper, ProductRepository productRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "delete-stock")
    public void handleEvent(Map<String, Object> eventMap) {
        DeleteStockEventDto deleteStockEventDto = objectMapper.convertValue(eventMap,
            DeleteStockEventDto.class);
        UUID hubId = deleteStockEventDto.getHubId();

        List<Product> productsTodelete = productRepository.findByHubId(hubId);
        if(productsTodelete != null && !productsTodelete.isEmpty()) {
            productsTodelete.forEach(productRepository::delete);
            logger.info("Deleted {} products for hubId {}", productsTodelete.size(), hubId);
        }
        else{
            logger.info("No products found for hubId {}", hubId);
        }
    }
}
