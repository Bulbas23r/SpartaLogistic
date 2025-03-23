package com.bulbas23r.client.product.infrastructure.messaging;

import com.bulbas23r.client.product.domain.model.Product;
import com.bulbas23r.client.product.domain.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.event.DeleteCompanyDto;
import common.event.UpdateStockEventDto;
import common.model.BaseEntity;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderEventConsumer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;


    public OrderEventConsumer(KafkaTemplate<String, Object> kafkaTemplate,
        ObjectMapper objectMapper, ProductRepository productRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "order-test-update-events")
    public void handleEvent(Map<String, Object> eventMap) {
        UpdateStockEventDto event = objectMapper.convertValue(eventMap, UpdateStockEventDto.class);
        System.out.println(event);
    }

    @Transactional
    @KafkaListener(topics = "delete-company")
    public void handleCompanyDeleteEvent(Map<String, Object> eventMap) {
        DeleteCompanyDto eventDto = objectMapper.convertValue(eventMap, DeleteCompanyDto.class);

        List<Product> productList = productRepository.findAllByCompanyId(eventDto.getCompanyId());

        productList.forEach(Product::setDeleted);
        productRepository.saveAll(productList);
    }
}
