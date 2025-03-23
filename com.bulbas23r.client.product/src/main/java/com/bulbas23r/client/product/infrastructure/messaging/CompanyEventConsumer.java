package com.bulbas23r.client.product.infrastructure.messaging;

import com.bulbas23r.client.product.domain.model.Product;
import com.bulbas23r.client.product.domain.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.event.DeleteCompanyEventDto;
import common.event.UpdateStockEventDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CompanyEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(CompanyEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    public CompanyEventConsumer(ObjectMapper objectMapper, ProductRepository productRepository) {
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "delete-company")
    public void handleDeleteCompanyEvent(Map<String, Object> eventMap) {
        DeleteCompanyEventDto deleteCompanyEventDto = objectMapper.convertValue(eventMap,
            DeleteCompanyEventDto.class);
        UUID companyId = deleteCompanyEventDto.getCompanyId();

        List<Product> productsToDelete = productRepository.findByCompanyId(companyId);
        if(productsToDelete != null && !productsToDelete.isEmpty()) {
            productsToDelete.forEach(productRepository::delete);
            logger.info("Deleted {} products for companyId {}", productsToDelete.size(), companyId);
        }
        else{
            logger.info("No products found for companyId {}", companyId);
        }
    }
}
