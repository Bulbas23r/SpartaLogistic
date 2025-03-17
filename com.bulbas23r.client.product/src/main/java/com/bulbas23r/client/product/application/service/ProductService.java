package com.bulbas23r.client.product.application.service;

import com.bulbas23r.client.product.domain.model.Product;
import com.bulbas23r.client.product.domain.repository.ProductRepository;
import com.bulbas23r.client.product.infrastructure.Const.ProductString;
import com.bulbas23r.client.product.presentation.dto.ProductCreateRequestDto;
import com.bulbas23r.client.product.presentation.dto.ProductUpdateRequestDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    //private final EventPublisher eventPublisher;// 이벤트 발행

    public Product createProduct(ProductCreateRequestDto productCreateRequestDto) {
        Product product = new Product(productCreateRequestDto);
        return productRepository.save(product);
    }

    public Product getProduct(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() ->
            new ResourceNotFoundException(ProductString.PRODUCT_NOT_FOUND)
        );
    }

    public Product updateProduct(UUID productId, ProductUpdateRequestDto productUpdateRequestDto) {
        Product product = getProduct(productId);
        product.update(productUpdateRequestDto);
        productRepository.update(product);
        return product;
    }

    public void deleteProduct(UUID productId) {
        Product product = getProduct(productId);
        product.delete();
        // TODO: soft-delete로 처리해야 하는데...
        productRepository.update(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
