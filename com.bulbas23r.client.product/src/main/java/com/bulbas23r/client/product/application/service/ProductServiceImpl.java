package com.bulbas23r.client.product.application.service;

import com.bulbas23r.client.product.domain.model.Product;
import com.bulbas23r.client.product.domain.repository.ProductQueryRepository;
import com.bulbas23r.client.product.domain.repository.ProductRepository;
import com.bulbas23r.client.product.infrastructure.Const.ProductString;
import com.bulbas23r.client.product.presentation.dto.ProductCreateRequestDto;
import com.bulbas23r.client.product.presentation.dto.ProductUpdateRequestDto;
import common.utils.PageUtils.CommonSortBy;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;
    //private final EventPublisher eventPublisher;// 이벤트 발행

    @Transactional
    @Override
    public Product createProduct(ProductCreateRequestDto productCreateRequestDto) {
        Product product = new Product(productCreateRequestDto);
        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product getProduct(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() ->
            new ResourceNotFoundException(ProductString.PRODUCT_NOT_FOUND)
        );
    }

    @Transactional
    @Override
    public Product updateProduct(UUID productId, ProductUpdateRequestDto productUpdateRequestDto) {
        Product product = getProduct(productId);
        product.update(productUpdateRequestDto);
        productRepository.update(product);
        return product;
    }

    @Transactional
    @Override
    public void deleteProduct(UUID productId) {
        Product product = getProduct(productId);
        product.delete();
        // TODO: soft-delete로 처리해야 하는데...
        productRepository.update(product);
    }

    @Transactional
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable, Direction sortDirection,
        CommonSortBy sortBy) {
        return productQueryRepository.searchProducts(keyword, pageable, sortDirection, sortBy);
    }
}
